package com.spirent.drools.service

import com.spirent.drools.config.dto.{ApplicationConfiguration, KafkaConfig}
import com.spirent.drools.config.{RedisRepositorySink, SessionCachingManager}
import com.spirent.drools.dto.events.NotValidEvent
import com.spirent.drools.dto.kpi.request.KpiRequest
import com.spirent.drools.service.kpi.impl.KpiServiceImpl
import com.spirent.drools.util.JsonObjectMapper.parseToKpiRequest
import com.spirent.drools.util.KafkaSink
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.header.internals.RecordHeader
import org.apache.log4j.LogManager
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.apache.spark.streaming.kafka010.{CanCommitOffsets, HasOffsetRanges}

import scala.util.{Failure, Success}

object VtaSessionLifecycleStreaming {

  private[this] final val log = LogManager.getLogger(this.getClass)

  def runStreaming(applicationConfiguration: ApplicationConfiguration,
                   ssc: StreamingContext,
                   kafkaInputStream: InputDStream[ConsumerRecord[String, String]]): Unit = {

    val ApplicationConfiguration(kafkaConfig, sparkConfig, redisConfig) = applicationConfiguration

    implicit val kafkaConfigBroadcast: Broadcast[KafkaConfig] = ssc.sparkContext.broadcast(kafkaConfig)
    implicit val kafkaSinkBroadcast: Broadcast[KafkaSink] = ssc.sparkContext.broadcast(KafkaSink(kafkaConfig.bootstrapServers))
    val sessionCachingManager = SessionCachingManager(() => RedisRepositorySink(redisConfig))
    implicit val scmBroadcast: Broadcast[SessionCachingManager] = ssc.sparkContext.broadcast(sessionCachingManager)

    kafkaInputStream
      .repartition(sparkConfig.partitionsNumber)
      .map(convertToValidEvent)
      .foreachRDD(rdd => rdd.foreach {

        case Right(request: KpiRequest) =>
          KpiServiceImpl.validateRules(request)
        case Left(notValidEvent) =>
          println(notValidEvent)
      })

    kafkaInputStream.foreachRDD { rdd =>
      val offsetRanges = rdd.asInstanceOf[HasOffsetRanges].offsetRanges
      kafkaInputStream.asInstanceOf[CanCommitOffsets].commitAsync(offsetRanges)
    }

    ssc.start()
    ssc.awaitTermination()
  }

  private def getMessageHeaders = {
    List(new RecordHeader("table", "session_lifecycle_events".getBytes))
  }

  //  private def processSessionEvent(maybeValidEvent: Either[NotValidEvent, TestSessionLifecycleEvent])
  //                                 (implicit sessionCachingManagerBroadcast: Broadcast[SessionCachingManager]): Either[NotValidEvent, TestSessionLifecycleEvent] = {
  //
  //    sessionCachingManagerBroadcast.value.processSessionLifecycleEvent(maybeValidEvent)
  //  }

  private def convertToValidEvent(kafkaRecord: ConsumerRecord[String, String]): Either[NotValidEvent, KpiRequest] = {
    parseToKpiRequest(kafkaRecord.value())
    match {
      case Success(request: KpiRequest) =>
        Right(request)

      case Failure(e) =>
        log.error(e.getMessage)
        Left(NotValidEvent(
          sourceJson = kafkaRecord.value(),
          errorDescription = Map(e.getMessage -> Option(e.getStackTrace)))
        )
    }
  }
}
