package com.spirent.drools.service.rule.impl

import com.spirent.drools.converter.impl.RuleContentConverterImpl
import com.spirent.drools.dao.impl.RuleDaoImpl
import com.spirent.drools.dto.rules.Rule
import com.spirent.drools.service.rule.RuleService
import org.kie.api.KieServices
import org.kie.api.builder.Message
import org.kie.api.runtime.KieContainer
import org.slf4j.LoggerFactory

import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 15.12.2021
 */
object RuleServiceImpl extends RuleService {
  private var kieContainer: KieContainer = _
  private val logger = LoggerFactory.getLogger(getClass)

  def setKieContainer(kieContainer: KieContainer): Unit = {
    this.kieContainer = kieContainer
  }

  def getKieContainer: KieContainer = {
    kieContainer
  }

  override def reloadRules: Unit = {
    setKieContainer(loadContainerFromRules(RuleDaoImpl.findAllRules))
  }

  def loadContainerFromRules(rules: ListBuffer[Rule]): KieContainer = {
    var startTime = System.currentTimeMillis
    val ks = KieServices.Factory.get
    val kr = ks.getRepository
    val kfs = ks.newKieFileSystem

    for (rule <- rules) {
      //todo must be model.
      val drl = RuleContentConverterImpl.clearSpecialSymbols(RuleContentConverterImpl.convertToString(rule.ruleContent))
      kfs.write("src/main/resources/" + drl.hashCode + ".drl", drl)
    }

    val kb = ks.newKieBuilder(kfs)

    kb.buildAll
    if (kb.getResults.hasMessages(Message.Level.ERROR)) {
      throw new RuntimeException("Build Errors:\n" + kb.getResults.toString)
    }
    val endTime = System.currentTimeMillis
    logger.info("Time to build rules : {} ms.", endTime - startTime)
    startTime = System.currentTimeMillis
    val container = ks.newKieContainer(kr.getDefaultReleaseId)
    logger.info("Time to load container: {} ms.", System.currentTimeMillis - startTime)
    container
  }
}
