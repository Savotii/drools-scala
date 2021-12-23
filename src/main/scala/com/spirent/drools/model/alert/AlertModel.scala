package com.spirent.drools.model.alert

import java.time.Instant
import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 22.12.2021
 */
class AlertModel {
   var id: Long = _
   var agentId: String = _
   var agentTestName: String = _
   var testSessionId: String = _
   var testId: String = _
   var agentTestId: String = _
   var workflowId: String = _
   var overlayId: String = _
   var networkElementId: String = _
   var category: String = _
   var pkg: String = _
   var testName: String = _
   var timestamp: Instant = _
   var alertId: String = _
   var alertName: String = _
   var level: String = _
   var failedKpis: ListBuffer[FailedKpiModel] = _
//
//  def setId(id: Long): Unit = this.id = id
//
//  def getId(): Long = id
//
//  def setAgentId(agentId: String): Unit = this.agentId = agentId
//
//  def getAgentId(): String = agentId
//
//  def setTestSessionId(testSessionId: String): Unit = this.testSessionId = testSessionId
//  def getTestSessionId(): String = testSessionId
//
//  def setAgentTestName(agentName: String): Unit = this.agentTestName = agentName
//
//  def getAgentTestName(): String = agentTestName
//
//  def setAgentTestId(agentTestId: String): Unit = this.agentTestId = agentId
//
//  def getAgentTestId(): String = agentTestId
//  

}
