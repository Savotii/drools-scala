package com.spirent.drools.dto

/**
 * @author ysavi2
 * @since 20.12.2021
 */
trait KpiAbstract {
  var agentId: String = _
  var agentTestName: String = _
  var testSessionId: String = _
  var testId: String = _
  var agentTestId: String = _
  var workflowId: String = _
  var overlayId: String = _

  //todo we do not know where is it from. by default will be equal to testId
  var networkElementId: String = _

  var category: String = _
  var pkg: String = _
  var testName: String = _
  var failed: Boolean = _
}
