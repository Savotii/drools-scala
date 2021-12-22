package com.spirent.drools.service.alert

import com.spirent.drools.dto.alert.{AlertCategory, AlertEvent, AlertLevel}
import com.spirent.drools.dto.kpi.FailedKpi
import org.kie.api.runtime.rule.Match

import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 22.12.2021
 */
trait AlertService {

  /*
   * We do not know how should we evaluate the levels
   * they might be evaluated by different circumstances for instance: region, latency, threshold...
   */
  def getAlertLevel: AlertLevel.level = AlertLevel.Critical

  def getCategory: AlertCategory.category = AlertCategory.Voice

  def send(alert: AlertEvent): Unit

  def buildFailedKpiLatencyAlert(matches: ListBuffer[Match]): ListBuffer[FailedKpi]

  def saveEvent(alert: AlertEvent): Unit

}
