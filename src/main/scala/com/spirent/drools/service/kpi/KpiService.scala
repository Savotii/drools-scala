package com.spirent.drools.service.kpi

import com.spirent.drools.dto.alert.AlertEvent
import com.spirent.drools.dto.kpi.Kpi
import com.spirent.drools.dto.kpi.request.KpiRequest
import com.spirent.drools.dto.rules.filter.KpiThresholdFilter

import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 22.12.2021
 */
trait KpiService {
  def validateRules(kpiRequest: KpiRequest): AlertEvent

  def getFilters: ListBuffer[KpiThresholdFilter]

  def updateFilters(): Unit
}
