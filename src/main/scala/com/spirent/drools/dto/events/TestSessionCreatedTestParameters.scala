package com.spirent.drools.dto.events

import com.fasterxml.jackson.annotation.{JsonIgnoreProperties, JsonProperty}

@JsonIgnoreProperties(ignoreUnknown = true)
case class TestSessionCreatedTestParameters(@JsonProperty("tc_type") tcType: String,
                                            @JsonProperty("tc_index") tcIndex: Int,
                                            @JsonProperty("topology_parameters") topologyParameters: Map[String, Any],
                                            @JsonProperty("session_parameters") sessionParameters: Map[String, Any])
