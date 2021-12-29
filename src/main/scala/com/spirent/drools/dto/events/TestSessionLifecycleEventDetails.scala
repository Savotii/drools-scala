package com.spirent.drools.dto.events

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonProperty}

@JsonIgnoreProperties(ignoreUnknown = true)
case class TestSessionLifecycleEventDetails(@JsonProperty("agent_id") agentId: String,
                                            @JsonProperty("agent_test_name") agentTestName: String,
                                            @JsonProperty("id") testSessionId: String,
                                            @JsonProperty("test_id") testId: String,
                                            @JsonProperty("agent_test_id") agentTestId: Option[String],
                                            @JsonProperty("workflow_id") workflowId: Option[String],
                                            @JsonProperty("overlay_id") overlayId: Option[String],
                                            @JsonProperty("category") category: Option[String],
                                            @JsonProperty("package") testPackage: Option[String],
                                            @JsonProperty("test_name") testName: Option[String],
                                            @JsonProperty("status") status: String,
                                            @JsonProperty("create_time") createTime: String,
                                            @JsonProperty("start_time") startTime: Option[String],
                                            @JsonProperty("end_time") endTime: Option[String],
                                            @JsonProperty("test_parameters") testParameters: Seq[TestSessionCreatedTestParameters],
                                            @JsonProperty("error_message") errorMessage: Option[String])
