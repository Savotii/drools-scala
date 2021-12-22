package com.spirent.drools.model.alert

import com.spirent.drools.dto.alert.{AlertCategory, AlertLevel}

import java.time.Instant
import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 22.12.2021
 */
case class AlertModel(id: Long,
                      agentId: String,
                      agentTestName: String,
                      testSessionId: String,
                      testId: String,
                      agentTestId: String,
                      workflowId: String,
                      overlayId: String,
                      networkElementId: String,
                      category: AlertCategory.category,
                      pkg: String,
                      testName: String,
                      timestamp: Instant,
                      alertId: String,
                      alertName: String,
                      level: AlertLevel.level,
                      failedKpis: ListBuffer[FailedKpiModel])
