package com.spirent.drools.config.kafka

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties
import scala.concurrent.{Await, Future, TimeoutException}

/**
 * @author ysavi2
 * @since 23.12.2021
 */
object Producer {

  val json = """{
    "agentId": "5Fsg54-09dd-8395-123a-dre3ca098084",
    "agentTestName": "N2N3SIPRegistration",
    "testSessionId": "748ad659-307f-458c-a62e-4feacc45e1bd",
    "testId": "7b659ea5-6110-4c16-b25f-b7b12886745",
    "agentTestId": "89bb7e5e-5153-11ec-bf63-0242ac130002",
    "workflowId": "cb6f234a-dec3-4374-87e7-baf44c16e2ef",
    "overlayId": "0e0c6dda-95bd-4720-898a-ffe5ca819338",
    "networkElementId": "f274b4d1-915e-4878-908a-051151c4b6b3",
    "category": "Voice",
    "package": "5G Core - N2N3 Assurance",
    "testName": "Registration",
    "kpis": [
    {
      "latency": 51000
    }""""""
  def send(): Unit = {
    val thread: Thread = new Thread() {
      val props: Properties = new Properties()
      props.put("bootstrap.servers", "localhost:29092")
      props.put("key.serializer",
        "org.apache.kafka.common.serialization.StringSerializer")
      props.put("value.serializer",
        "org.apache.kafka.common.serialization.StringSerializer")
      props.put("acks", "all")
      val producer = new KafkaProducer[String, String](props)
      val topic = "text_topic"
      try {
        for (i <- 0 to 150) {
          val record = new ProducerRecord[String, String](topic, i.toString, json)
          val metadata = producer.send(record)
          printf(s"sent record(key=%s value=%s) " +
            "meta(partition=%d, offset=%d)\n",
            record.key(), record.value(),
            metadata.get().partition(),
            metadata.get().offset())
        }
      } catch {
        case e: Exception => e.printStackTrace()
      } finally {
        producer.close()
      }
    }
    thread.start()
  }
}
