package com.spirent.drools.config.kafka

import com.fasterxml.jackson.databind.ObjectMapper

import java.util.UUID
import scala.util.Random

/**
 * @author ysavi2
 * @since 28.12.2021
 */
object FakeMessageGenerator {
  private val mapper: ObjectMapper = new ObjectMapper
  private val template =
    "{ " +
      "agentId: %s," +
      "agentTestName:%s," +
      "testSessionId:%s," +
      "testId:%s," +
      "agentTestId: %s," +
      "workflowId: %s," +
      "overlayId: %s," +
      "networkElementId: %s," +
      "category: Voice," +
      "package: 5G Core - N2N3 Assurance," +
      "testName: Registration," +
      "kpis: [ " +
             " { " +
                "latency: %s" +
                  " } " +
             " ] " +
    "}"

  private def generateLatency: Long = {
    val random = new Random()
    val int: Long = random.between(10000, 70000)
    println("New latency = " + int)
    int
  }

  def generateMessage: String = {
    //    val request = new KpiRequest
    //    request.agentId = UUID.randomUUID().toString
    //    request.agentTestName = UUID.randomUUID().toString
    //    request.testSessionId = UUID.randomUUID().toString
    //    request.testId = UUID.randomUUID().toString
    //    request.agentTestId = UUID.randomUUID().toString
    //    request.workflowId = UUID.randomUUID().toString
    //    request.overlayId = UUID.randomUUID().toString
    //    request.networkElementId = UUID.randomUUID().toString
    //    request.category = "Voice"
    //    request.pkg = "5G Core - N2N3 Assurance"
    //    request.testName = "Registration"
    //
    //    val latency = new KpiLatency
    //    latency.latency = generateLatency
    //    request.kpis.addOne(latency)
    //    mapper.writeValueAsString(request)

    String.format(template,
      UUID.randomUUID().toString,
      UUID.randomUUID().toString,
      UUID.randomUUID().toString,
      UUID.randomUUID().toString,
      UUID.randomUUID().toString,
      UUID.randomUUID().toString,
      UUID.randomUUID().toString,
      UUID.randomUUID().toString,
      generateLatency)
  }
}
