package com.spirent.drools.service.ruleengine

import com.spirent.drools.dto.kpi.Kpi
import com.spirent.drools.dto.kpi.request.KpiRequest
import org.kie.api.runtime.rule.Match

import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 22.12.2021
 */
trait RuleEngineService {
  def fireAllRules(kpiRequest: Kpi): ListBuffer[Match]

  def fireAllRules(kpiRequest: KpiRequest): ListBuffer[Match]
}
