package com.spirent.drools.dto.kpi.request

import com.spirent.drools.dto.KpiAbstract
import com.spirent.drools.dto.kpi.KpiLatency

import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 22.12.2021
 */
case class KpiRequest(kpis: ListBuffer[KpiLatency]) extends KpiAbstract()
