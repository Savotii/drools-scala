package com.spirent.drools.service.ruleengine.impl

import com.spirent.drools.dto.kpi.Kpi
import com.spirent.drools.dto.kpi.request.KpiRequest
import com.spirent.drools.service.ruleengine.RuleEngineService
import org.kie.api.KieServices
import org.kie.api.builder.{KieFileSystem, KieRepository}
import org.kie.api.runtime.KieContainer
import org.kie.api.runtime.rule.Match

import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 22.12.2021
 */
object DroolsEngineServiceImpl extends RuleEngineService {
  private var kieServices: KieServices = KieServices.Factory.get()
  private var kieContainer: KieContainer = buildKieContainer()

  def buildKieContainer(): KieContainer = {
    val kieRepository:KieRepository = KieServices.Factory.get().getRepository
    kieRepository.addKieModule(kieRepository.getDefaultReleaseId)

    val kieFileSystem: KieFileSystem = {
      val fileSystem = KieServices.Factory.get().newKieFileSystem

      //todo fill the rules from db.
      fileSystem
    }

  }


  override def fireAllRules(kpiRequest: Kpi): ListBuffer[Match] = ???

  override def fireAllRules(kpiRequest: KpiRequest): ListBuffer[Match] = ???
}
