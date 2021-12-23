package com.spirent.drools.service.kpi.impl

import com.spirent.drools.dao.impl.FilterRepositoryDao
import com.spirent.drools.dto.alert.AlertEvent
import com.spirent.drools.dto.kpi.request.KpiRequest
import com.spirent.drools.dto.rules.filter.KpiThresholdFilter
import com.spirent.drools.service.alert.impl.AlertServiceImpl
import com.spirent.drools.service.kpi.KpiService
import com.spirent.drools.service.ruleengine.impl.DroolsEngineServiceImpl
import com.spirent.drools.util.RuleFilterUtil
import org.kie.api.runtime.rule.Match
import io.bfil.automapper._

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
    filters.addAll(FilterRepositoryDao.findAll.map(filterModel => automap(filterModel).to[KpiThresholdFilter]))
  }

  private def buildAlertMessageAndSend(request: KpiRequest, matches: ListBuffer[Match]): AlertEvent = {
    val alert = automap(request).to[AlertEvent]
    alert.alertId = UUID.randomUUID.toString
    alert.failedKpis = AlertServiceImpl.buildFailedKpiLatencyAlert(matches)
    AlertServiceImpl.saveEvent(alert)
    AlertServiceImpl.send(alert)
    alert
  }
}
