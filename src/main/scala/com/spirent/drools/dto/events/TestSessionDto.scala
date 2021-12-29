package com.spirent.drools.dto.events

case class TestSessionDto(agentId: String,
                          agentTestName: String,
                          testSessionId: String,
                          testId: String,
                          agentTestId: Option[String],
                          workflowId: Option[String],
                          overlayId: Option[String],
                          category: Option[String],
                          testPackage: Option[String],
                          testName: Option[String],
                          status: String,
                          createTime: String,
                          startTime: Option[String] = Option.empty,
                          endTime: Option[String] = Option.empty,
                          agentTestStagesConfig: AgentTestStagesConfig)
