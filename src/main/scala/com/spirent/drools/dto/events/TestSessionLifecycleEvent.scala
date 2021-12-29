package com.spirent.drools.dto.events

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonProperty}

@JsonIgnoreProperties(ignoreUnknown = true)
case class TestSessionLifecycleEvent(@JsonProperty("event_type") eventType: String,
                                     @JsonProperty("event_time") eventTime: String,
                                     @JsonProperty("test_session") testSession: TestSessionLifecycleEventDetails)
