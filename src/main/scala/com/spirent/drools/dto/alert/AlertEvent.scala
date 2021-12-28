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

  override def toString = s"AlertEvent(timestamp=$timestamp, alertId=$alertId, name=$name, level=$level, failedKpis=$failedKpis)"

  def toJSON(): String = {
    val sbAlert: StringBuilder = new StringBuilder
    val sbKpi: StringBuilder = new StringBuilder
    convertKpisToJson(sbKpi)
    convertAlertToJson(sbAlert, sbKpi)
    sbAlert.toString()
  }

  private def convertAlertToJson(sbAlert: StringBuilder, sbKpis: StringBuilder): Unit = {
    sbAlert.append("{").append("\n")
    sbAlert.append("agentId").append(":").append(agentId).append(",").append("\n")
    sbAlert.append("agentTestName").append(":").append(agentTestName).append(",").append("\n")
    sbAlert.append("testSessionId").append(":").append(testSessionId).append(",").append("\n")
    sbAlert.append("testId").append(":").append(testId).append(",").append("\n")
    sbAlert.append("agentTestId").append(":").append(agentTestId).append(",").append("\n")
    sbAlert.append("overlayId").append(":").append(overlayId).append(",").append("\n")
    sbAlert.append("workflowId").append(":").append(workflowId).append(",").append("\n")
    sbAlert.append("networkElementId").append(":").append(networkElementId).append(",").append("\n")
    sbAlert.append("category").append(":").append(category).append(",").append("\n")
    sbAlert.append("package").append(":").append(pkg).append(",").append("\n")
    sbAlert.append("testName").append(":").append(testName).append(",").append("\n")
    sbAlert.append("timestamp").append(":").append(timestamp).append(",").append("\n")
    sbAlert.append("alertId").append(":").append(alertId).append(",").append("\n")
    sbAlert.append("level").append(":").append(level).append(",").append("\n")
    sbAlert.append("failed").append(":").append(failed).append(",").append("\n")
    sbAlert.append(sbKpis.toString)
    sbAlert.append("}").append("\n")
  }

  private def convertKpisToJson(sbKpi: StringBuilder): Unit = {
    sbKpi.append("kpis: [").append("\n")
    for (kpi <- failedKpis) {
      sbKpi
        .append("{").append("\n")
        .append("name").append(":").append(kpi.name).append(",").append("\n")
        .append("threshold").append(":").append(kpi.threshold).append(",").append("\n")
        .append("latency").append(":").append(kpi.latency).append(",").append("\n")
        .append("}").append("\n")
    }
    sbKpi.append("]\n")
  }
}