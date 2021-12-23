package com.spirent.drools.dto.kpi

import lombok.Data
import lombok.experimental.Accessors

/**
 * @author ysavi2
 * @since 22.12.2021
 */
@Data
@Accessors(chain = true)
class KpiLatency {
  var threshold: Long = _
  var id: String = _
  var latency: Long = _
}
