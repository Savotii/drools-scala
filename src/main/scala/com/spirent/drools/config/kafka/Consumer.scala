package com.spirent.drools.config.kafka

import com.fasterxml.jackson.databind.ObjectMapper
import com.spirent.drools.dto.kpi.KpiLatency
import com.spirent.drools.dto.kpi.request.KpiRequest
import com.spirent.drools.service.kpi.impl.KpiServiceImpl
import com.spirent.drools.service.ruleengine.impl.DroolsEngineServiceImpl
import org.apache.kafka.clients.consumer.KafkaConsumer

import java.time.Duration
import java.util
import java.util.Properties
import java.util.concurrent.CompletableFuture
import scala.jdk.CollectionConverters.IterableHasAsScala

/**
 * @author ysavi2
 * @since 23.12.2021
 */
object Consumer {
  private val mapper: ObjectMapper = new ObjectMapper

  def listen(): Unit = {
    CompletableFuture.runAsync(() => {
      //externalize the props
      val props: Properties = new Properties()
      props.put("group.id", "test")
      props.put("bootstrap.servers", "localhost:29092")
      props.put("key.deserializer",
        "org.apache.kafka.common.serialization.StringDeserializer")
      props.put("value.deserializer",
        "org.apache.kafka.common.serialization.StringDeserializer")
      props.put("enable.auto.commit", "true")
            props.put("auto.commit.interval.ms", "1000")
      val consumer = new KafkaConsumer(props)
      val topics: util.List[String] = util.List.of("text_topic")
      try {
        consumer.subscribe(topics)
        while (true) {
          val records = consumer.poll(Duration.ofMillis(10))
          if (!records.isEmpty) {
            for (record <- records.asScala) {
              println("Topic: " + record.topic() +
                ",Key: " + record.key() +
                ",Value: " + record.value() +
                ", Offset: " + record.offset() +
                ", Partition: " + record.partition())

              try {
                val res: Array[String] = record.value().toString.replace("}", "").replace("{", "").replace(" ", "").split(",")
                val kpiRequest: KpiRequest = buildKpiRequest(res)
                KpiServiceImpl.validateRules(kpiRequest)
              } catch {
                case e: Exception => println(e)
              }
            }
          }
        }
      } catch {
        case e: Exception => e.printStackTrace()
      } finally {
        consumer.close()
      }
    })
  }

  def buildKpiRequest(res: Array[String]): KpiRequest = {
    val kpiRequest = new KpiRequest

    for (str <- res) {

      if (str.contains("kpis")) {
        val trimmed = str.replace("kpis:[", "").replace("]", "").trim
        val splittedKpis = trimmed.split(":")
        val latency = new KpiLatency
        latency.latency = splittedKpis(1).trim.toLong
        kpiRequest.kpis.addOne(latency)
      } else {

        val splitted: Array[String] = str.split(":")
        val column: Predef.String = new Predef.String(splitted(0))
        val value: Predef.String = new Predef.String(splitted(1)) // to scala String

        if (column.equals("agentId")) {
          kpiRequest.agentId = value
        }
        if (column.equals("agentTestName")) {
          kpiRequest.agentTestName = value
        }
        if (column.equals("testSessionId")) {
          kpiRequest.testSessionId = value
        }
        if (column.equals("testId")) {
          kpiRequest.testId = value
        }
        if (column.equals("agentTestId")) {
          kpiRequest.agentTestId = value
        }
        if (column.equals("workflowId")) {
          kpiRequest.workflowId = value
        }
        if (column.equals("overlayId")) {
          kpiRequest.overlayId = value
        }
        if (column.equals("networkElementId")) {
          kpiRequest.networkElementId = value
        }
        if (column.equals("category")) {
          kpiRequest.category = value
        }
        if (column.equals("package")) {
          kpiRequest.pkg = value
        }
        if (column.equals("testName")) {
          kpiRequest.testName = value
        }
      }
    }
    kpiRequest
  }

}
