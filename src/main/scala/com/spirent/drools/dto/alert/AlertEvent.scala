package com.spirent.drools.dto.alert

import com.spirent.drools.dto.KpiAbstract
import com.spirent.drools.dto.kpi.FailedKpi
import io.swagger.v3.oas.annotations.media.Schema
import lombok.experimental.Accessors
import lombok.{Data, EqualsAndHashCode, ToString}

import java.time.Instant

/**
 * @author ysavi2
 * @since 16.12.2021
 */
@Data
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
class AlertEvent extends KpiAbstract {

  @Schema(description = "Date of the raised incident.", example = "2021-12-10T13:12:10Z")
  var timestamp: Instant = Instant.now
  @Schema(description = "UUID")
  var alertId: String = _
  @Schema(description = "Rule which has been failed.")
  var name: String
  = "High latency alert"
  @Schema(description = "alert type", example = "CRITICAL, WARN etc.")
  var level: AlertLevel.level = _
  @Schema(description = "List of the failed kpis", `type` = "array",
    example = "{\n" +
      "      \"id\": \"hostLatency\",\n" +
      "      \"value\": 34534,\n" +
      "      \"threshold\" : 1000\n" +
      "    }")
  var failedKpis: scala.collection.mutable.ListBuffer[FailedKpi] = new scala.collection.mutable.ListBuffer[FailedKpi]()
}