package com.spirent.drools.config.kafka

import org.apache.kafka.clients.consumer.KafkaConsumer

import java.time.Duration
import java.util
import java.util.Properties
import java.util.List
import scala.collection.mutable.ListBuffer
import scala.jdk.CollectionConverters.IterableHasAsScala

/**
 * @author ysavi2
 * @since 23.12.2021
 */
object Consumer {

  //externalize the props
  val props: Properties = new Properties()
  props.put("group.id", "test")
  props.put("bootstrap.servers", "localhost:9092")
  props.put("key.deserializer",
    "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("value.deserializer",
    "org.apache.kafka.common.serialization.StringDeserializer")
  props.put("enable.auto.commit", "true")
  props.put("auto.commit.interval.ms", "1000")
  val consumer = new KafkaConsumer(props)
  val topics: util.List[String] = List.of("topic_text")
  try {
    consumer.subscribe(topics)
    while (true) {
      val records = consumer.poll(Duration.ofMillis(10))
      for (record <- records.asScala) {
        println("Topic: " + record.topic() +
          ",Key: " + record.key() +
          ",Value: " + record.value() +
          ", Offset: " + record.offset() +
          ", Partition: " + record.partition())
      }
    }
  } catch {
    case e: Exception => e.printStackTrace()
  } finally {
    consumer.close()
  }
}
