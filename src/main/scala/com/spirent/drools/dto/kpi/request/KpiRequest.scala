package com.spirent.drools.dto.kpi.request

import com.spirent.drools.dto.KpiAbstract
import com.spirent.drools.dto.kpi.KpiLatency

/**
 * @author ysavi2
 * @since 22.12.2021
 */
case class KpiRequest(kpis: scala.collection.mutable.ListBuffer[KpiLatency]) extends KpiAbstract()
