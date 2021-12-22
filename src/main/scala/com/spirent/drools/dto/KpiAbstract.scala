package com.spirent.drools.dto

/**
 * @author ysavi2
 * @since 20.12.2021
 */
trait KpiAbstract {
  protected var agentId: String = _
  protected var agentTestName: String = _
  protected var testSessionId: String = _
  protected var testId: String = _
  protected var agentTestId: String = _
  protected var workflowId: String = _
  protected var overlayId: String = _

  //todo we do not know where is it from. by default will be equal to testId
  protected var networkElementId: String = _

  protected var category: String = _
  protected var pkg: String = _
  protected var testName: String = _
  protected var failed: Boolean = _
}
