package com.spirent.drools.service.notification.impl

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.spirent.drools.service.notification.NotificationService
import org.apache.kafka.clients.producer.{KafkaProducer, Producer}

/**
 * @author ysavi2
 * @since 23.12.2021
 */
object KafkaNotificationServiceImpl extends NotificationService {
//  private var producer:KafkaProducer = _
//  private val mapper:ObjectMapper =  ObjectMapper

  override def send(obj: AnyRef): Unit = {
//    try {
//      producer.send(mapper.writeValueAsString(obj))
//    } catch {
//      case e: JsonProcessingException => println(e)
//    }
    println("")
  }
}
