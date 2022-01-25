package com.spirent.drools.service.kpi.impl

import com.spirent.drools.dao.impl.FilterRepositoryDao
import com.spirent.drools.dto.alert.{AlertEvent, AlertLevel}
import com.spirent.drools.dto.kpi.request.KpiRequest
import com.spirent.drools.dto.rules.filter.KpiThresholdFilter
import com.spirent.drools.service.alert.impl.AlertServiceImpl
import com.spirent.drools.service.kpi.KpiService
import com.spirent.drools.service.ruleengine.impl.DroolsEngineServiceImpl
import com.spirent.drools.util.RuleFilterUtil
import io.bfil.automapper._
import org.kie.api.runtime.rule.Match

import java.time.Instant
import java.util.UUID
import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 23.12.2021
 */
object KpiServiceImpl extends KpiService {
  """
    |todo extract it to the dedicated class
    |""".stripMargin
  private val filters: ListBuffer[KpiThresholdFilter] = new ListBuffer[KpiThresholdFilter]


  override def validateRules(kpiRequest: KpiRequest): AlertEvent = {
    RuleFilterUtil.populateThreshold(kpiRequest, filters)
    val result = DroolsEngineServiceImpl.fireAllRules(kpiRequest)
    if (result.nonEmpty) {
      buildAlertMessageAndSend(kpiRequest, result)
    }
    null
  }

  override def getFilters: ListBuffer[KpiThresholdFilter] = {
    filters.clone()
  }

  override def updateFilters(): Unit = {
    filters.clear()
    //    filters.addAll(FilterRepositoryDao.findAll.map(filterModel => automap(filterModel).to[KpiThresholdFilter]))
    //todo will it work here as intended? += will the whole list be added?
    //    filters += FilterRepositoryDao.findAll.map(filterModel => automap(filterModel).to[KpiThresholdFilter])
    FilterRepositoryDao.findAll.map(filterModel => automap(filterModel).to[KpiThresholdFilter])
      .foreach(r => filters += r)
  }

  def mapToAlert(request: KpiRequest, matches: ListBuffer[Match]) = {
    val alert = new AlertEvent()
    alert.alertId = UUID.randomUUID.toString
    alert.failedKpis = AlertServiceImpl.buildFailedKpiLatencyAlert(matches)
    alert.testSessionId = request.testSessionId
    alert.agentId = request.agentId
    alert.agentTestId = request.agentTestId
    alert.agentTestName = request.agentTestName
    alert.category = request.category
    alert.level = AlertLevel.Critical
    alert.networkElementId = request.networkElementId
    alert.pkg = request.pkg
    alert.overlayId = request.overlayId
    alert.timestamp = Instant.now
    alert.workflowId = request.workflowId
    alert.name = request.testName
    alert
  }

  private def buildAlertMessageAndSend(request: KpiRequest, matches: ListBuffer[Match]): AlertEvent = {
    val alert = mapToAlert(request, matches)
    AlertServiceImpl.saveEvent(alert)
    AlertServiceImpl.send(alert)
    alert
  }
}
