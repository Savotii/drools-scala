package com.spirent.drools.service.ruleengine.impl

import com.spirent.drools.converter.impl.RuleContentConverterImpl
import com.spirent.drools.dao.impl.RuleDaoImpl
import com.spirent.drools.dto.kpi.Kpi
import com.spirent.drools.dto.kpi.request.KpiRequest
import com.spirent.drools.dto.rules.globals.{GlobalBoolean, GlobalRuleCounter}
import com.spirent.drools.listeners.TrackingAgendaEventListener
import com.spirent.drools.model.rule.RuleModel
import com.spirent.drools.service.ruleengine.RuleEngineService
import com.spirent.drools.util.RuleFilterUtil
import org.apache.commons.lang3.StringUtils
import org.kie.api.KieServices
import org.kie.api.builder.{KieFileSystem, KieRepository}
import org.kie.api.runtime.rule.Match
import org.kie.api.runtime.{KieContainer, KieSession}

import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 22.12.2021
 */
object DroolsEngineServiceImpl extends RuleEngineService {
  val SRC_MAIN_RESOURCES_PATH = "src/main/resources/"
  val DRL_EXTENSION = ".drl"

  private var kieFileSystem: KieFileSystem = _
  private var kieSession: KieSession = _
  private var kieContainer: KieContainer = _


  override def fireAllRules(kpiRequest: Kpi): ListBuffer[Match] = {
    kieSession.addEventListener(TrackingAgendaEventListener)
    kieSession.insert(kpiRequest)
    kieSession.fireAllRules
    val matches: ListBuffer[Match] = TrackingAgendaEventListener.getMatchList
    TrackingAgendaEventListener.reset()
    matches
  }

  override def fireAllRules(kpiRequest: KpiRequest): ListBuffer[Match] = {
    kpiRequest.kpis.foreach(kieSession.insert)
    kieSession.fireAllRules
    val matchList: ListBuffer[Match] = TrackingAgendaEventListener.getMatchList
    TrackingAgendaEventListener.reset()
    matchList
  }

  private def getRulesContent(ruleModels: ListBuffer[RuleModel]): Map[String, String] = {
    ruleModels.map(rule => rule.ruleKey -> RuleContentConverterImpl.clearSpecialSymbols(rule.content)).toMap
  }

  private def getPath(ruleKey: String): String = SRC_MAIN_RESOURCES_PATH + getRuleTextFileName(ruleKey) + DRL_EXTENSION

  def getRuleTextFileName(ruleKey: String): String = ruleKey.replace(StringUtils.SPACE, "_")

  def rebuildWholeContext(): Unit = {
    val kieRepository: KieRepository = KieServices.Factory.get().getRepository

    kieFileSystem = {
      val fileSystem = KieServices.Factory.get().newKieFileSystem
      //todo fill the rules from db.
      fileSystem
    }

    updateFileSystemByRules()
    rebuildFileSystem()
    kieContainer = rebuildContainer(kieRepository)
    rebuildSession(kieContainer)
    setGlobal()
  }

  def rebuildFileSystem(): Unit = {
    val kieBuilder = KieServices.Factory.get().newKieBuilder(kieFileSystem)
    kieBuilder.buildAll
  }

  def updateFileSystemByRules(): Unit = {
    val rules: Map[String, String] = getRulesContent(RuleDaoImpl.findAllRuleModel)
    rules.foreach(rule => kieFileSystem.write(getPath(rule._1), rule._2))
  }

  def rebuildContainer(kieRepository: KieRepository): KieContainer = KieServices.Factory.get().newKieContainer(kieRepository.getDefaultReleaseId)

  def rebuildSession(container: KieContainer): Unit = {
    kieSession = container.newKieSession()
    kieSession.addEventListener(TrackingAgendaEventListener)
  }

  def setGlobal(): Unit = {
    try {
      kieSession.setGlobal("flag", GlobalBoolean)
      kieSession.setGlobal("counter", GlobalRuleCounter)
    } catch {
      case e: Exception => println(e)
    }
  }
}