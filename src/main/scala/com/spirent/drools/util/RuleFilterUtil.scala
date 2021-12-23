package com.spirent.drools.util

import com.spirent.drools.dto.kpi.KpiLatency
import com.spirent.drools.dto.kpi.request.KpiRequest
import com.spirent.drools.dto.rules.filter.KpiThresholdFilter

import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 23.12.2021
 */
object RuleFilterUtil {
  def populateThreshold(request: KpiRequest, filters: ListBuffer[KpiThresholdFilter]): Unit = {
    val basedThreshold: Option[KpiThresholdFilter] =
      filters.filter(
        (thr: KpiThresholdFilter) => thr.overlayId == null && thr.testId == null)
        .lastOption
    val dedicatedThreshold: Option[KpiThresholdFilter] =
      filters.filter(
        (thr: KpiThresholdFilter) => request.testId.equals(thr.testId) && request.overlayId.equals(thr.overlayId))
        .lastOption
    request.kpis.foreach((kpi: KpiLatency) =>
      kpi.threshold = dedicatedThreshold.getOrElse(basedThreshold.get).value)
  }
}
