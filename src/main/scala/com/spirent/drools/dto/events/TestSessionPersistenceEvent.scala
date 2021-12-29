package com.spirent.drools.dto.events

case class TestSessionPersistenceEvent(id: String,
                                       agentId: String,
                                       agentTestName: String,
                                       testSessionId: String,
                                       testId: String,
                                       eventType: String,
                                       agentTestId: Option[String],
                                       workflowId: Option[String],
                                       overlayId: Option[String],
                                       testCategory: Option[String],
                                       testPackage: Option[String],
                                       testName: Option[String],
                                       status: String,
                                       eventTimestamp: String)