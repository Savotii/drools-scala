package com.spirent.drools.config.kafka

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerRecord}

import java.util.Properties
import java.util.concurrent.CompletableFuture

/**
 * @author ysavi2
 * @since 23.12.2021
 */
object Producer {
  def send(): Unit = {
    CompletableFuture.runAsync(() => {
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
        while (true) {
          val record = new ProducerRecord[String, String](topic, FakeMessageGenerator.generateMessage)
          val metadata = producer.send(record)
          printf(s"sent record(key=%s value=%s) " +
            "meta(partition=%d, offset=%d)\n",
            record.key(), record.value(),
            metadata.get().partition(),
            metadata.get().offset())
          Thread.sleep(5000)
        }
      } catch {
        case e: Exception => e.printStackTrace()
      } finally {
        producer.close()
      }
    })
  }

  def sendAlertNotification(obj: String): Unit = {
    val props: Properties = new Properties()
    props.put("bootstrap.servers", "localhost:29092")
    props.put("key.serializer",
      "org.apache.kafka.common.serialization.StringSerializer")
    props.put("value.serializer",
      "org.apache.kafka.common.serialization.StringSerializer")
    props.put("acks", "all")
    val producer = new KafkaProducer[String, String](props)
    val topic = "alert_notification"
    try {
      val record = new ProducerRecord[String, String](topic, obj)
      val metadata = producer.send(record)
      printf(s"sent record(key=%s value=%s) " +
        "meta(partition=%d, offset=%d)\n",
        record.key(), record.value(),
        metadata.get().partition(),
        metadata.get().offset())
    } catch {
      case e: Exception => e.printStackTrace()
    } finally {
      producer.close()
    }
  }
}
