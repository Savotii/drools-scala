package com.spirent.drools.util

import com.fasterxml.jackson.databind._
import com.fasterxml.jackson.module.scala.{DefaultScalaModule, ScalaObjectMapper}
import com.spirent.drools.dto.events.{AgentTestStagesConfig, TestSessionDto, TestSessionLifecycleEvent}
import com.spirent.drools.dto.kpi.request.KpiRequest
import org.apache.log4j.LogManager

import scala.util.{Failure, Success, Try}

object JsonObjectMapper {

  private[this] final val log = LogManager.getLogger(this.getClass)

  private val mapper = new ObjectMapper() with ScalaObjectMapper
  mapper.registerModule(DefaultScalaModule)
  mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
  mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)

  def parseToKpiRequest(sourceJson: String): Try[KpiRequest] = {
    Try(mapper.readValue[KpiRequest](sourceJson))
  }

  def parseToTestMeasurementList(sourceJson: String): Try[Seq[TestSessionLifecycleEvent]] = {
    Try(mapper.readValue[Seq[TestSessionLifecycleEvent]](sourceJson))
  }

  def parseToTestSessionDto(sourceJson: String): Option[TestSessionDto] = {
    Try(mapper.readValue[TestSessionDto](sourceJson)) match {
      case Success(value) =>
        Option(value)
      case Failure(exception) =>
        log.error(exception.getMessage)
        Option.empty
    }
  }

  def parseToAgentTestStagesConfig(sourceJson: String): Option[AgentTestStagesConfig] = {
    Try(mapper.readValue[AgentTestStagesConfig](sourceJson)) match {
      case Success(value) =>
        Option(value)
      case Failure(exception) =>
        log.error(exception.getMessage)
        Option.empty
    }
  }

  def toJson(sourceObject: AnyRef): String = {
    mapper.writeValueAsString(sourceObject)
  }

}