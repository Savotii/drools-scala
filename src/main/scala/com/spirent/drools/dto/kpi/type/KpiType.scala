package com.spirent.drools.dto.kpi.`type`

/**
 * @author ysavi2
 * @since 22.12.2021
 */
object KpiType extends Enumeration {
  type _type = Value
  val Host_Latency: KpiType.Value = Value(1, "hostLatency")
}
