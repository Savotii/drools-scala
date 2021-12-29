package com.spirent.drools.config

import com.spirent.drools.config.dto.{RedisConfig, RedisEntityPrefixes}
import com.spirent.drools.dto.events._
import com.spirent.drools.util.JsonObjectMapper
import com.spirent.drools.util.JsonObjectMapper.{parseToAgentTestStagesConfig, toJson}
import org.apache.log4j.LogManager
import redis.clients.jedis.Jedis

import scala.util.Try

/**
 * @author ysavi2
 * @since 28.12.2021
 */
class RedisRepository(redisClient: Jedis, redisEntityPrefixes: RedisEntityPrefixes) extends Serializable {

  private[this] lazy val log = LogManager.getLogger(this.getClass)

  def cacheSession(implicit testSessionLifecycleEvent: TestSessionLifecycleEvent): TestSessionLifecycleEvent = {
    val eventKey = getSessionEventKey
    val cacheResponseStatus = redisClient.set(eventKey, JsonObjectMapper.toJson(convertSessionCreatedEventToTestSessionDto))
    log.info(s"Caching status for session with id ${testSessionLifecycleEvent.testSession.testSessionId} is: $cacheResponseStatus")

    val ttlResponseStatus = redisClient.expire(eventKey, 60 * 60 * 24L)
    log.info(s"Set TTL for session with id ${testSessionLifecycleEvent.testSession.testSessionId} to 24 hours is: ${if (ttlResponseStatus == 1L) "SUCCESS" else "FAILED"}")

    testSessionLifecycleEvent
  }

  def updateSessionStartTime(implicit testSessionLifecycleEvent: TestSessionLifecycleEvent): TestSessionLifecycleEvent = {
    val eventKey = getSessionEventKey
    val value = Try(redisClient.get(eventKey)).toOption

    val isSaved = value
      .flatMap(JsonObjectMapper.parseToTestSessionDto)
      .map(_.copy(startTime = testSessionLifecycleEvent.testSession.startTime))
      .map(sessionEvent => redisClient.set(getSessionEventKey, toJson(sessionEvent)))
      .getOrElse(0)

    if (isSaved != "OK") {
      log.error(s"Failed to update start time for session: ${testSessionLifecycleEvent.testSession.testSessionId}")
    }
    testSessionLifecycleEvent
  }

  def updateSessionEndTime(implicit testSessionLifecycleEvent: TestSessionLifecycleEvent): TestSessionLifecycleEvent = {
    val eventKey = getSessionEventKey
    val value = Try(redisClient.get(eventKey)).toOption

    val isSaved = value
      .flatMap(JsonObjectMapper.parseToTestSessionDto)
      .map(_.copy(endTime = testSessionLifecycleEvent.testSession.endTime))
      .map { sessionEvent =>
        redisClient.set(getSessionEventKey, toJson(sessionEvent))
        redisClient.expire(eventKey, 60 * 60L)
      }
      .getOrElse(0)

    if (isSaved == 0) {
      log.error(s"Failed to update end time for session: ${testSessionLifecycleEvent.testSession.testSessionId}")
    }
    testSessionLifecycleEvent
  }

  private def getTestStages(agentTestId: String): Option[AgentTestStagesConfig] = {
    val testStageConfigKey = s"${redisEntityPrefixes.agentTestStagesConfigPrefix}_$agentTestId"

    Try(redisClient.get(testStageConfigKey))
      .toOption
      .flatMap(parseToAgentTestStagesConfig)
  }

  private def getDefaultTestStages: AgentTestStagesConfig = {
    AgentTestStagesConfig(
      Seq(
        TestStage(testStageName = "Registration complete",
          testStageStatements = Seq(
            TestStageStatement(kpiName = "UeMmRegCmp".toLowerCase, kpiType = "int", statement = "gt", statementValue = 0),
            TestStageStatement(kpiName = "UeMmRegRej".toLowerCase, kpiType = "int", statement = "gt", statementValue = 0)
          )
        ),
        TestStage(testStageName = "POC establishing accept",
          testStageStatements = Seq(
            TestStageStatement(kpiName = "UeSmPduEstAccp".toLowerCase, kpiType = "int", statement = "lt", statementValue = 0))
        )
      )
    )
  }

  private def convertSessionCreatedEventToTestSessionDto(implicit testSessionLifecycleEvent: TestSessionLifecycleEvent): Option[TestSessionDto] = {
    Option(testSessionLifecycleEvent.testSession)
      .map { testSessionDetails =>
        TestSessionDto(
          agentId = testSessionDetails.agentId,
          agentTestName = testSessionDetails.agentTestName,
          testSessionId = testSessionDetails.testSessionId,
          testId = testSessionDetails.testId,
          agentTestId = testSessionDetails.agentTestId,
          workflowId = testSessionDetails.workflowId,
          overlayId = testSessionDetails.overlayId,
          category = testSessionDetails.category,
          testPackage = testSessionDetails.testPackage,
          testName = testSessionDetails.testName,
          status = testSessionDetails.status,
          createTime = testSessionDetails.createTime,
          agentTestStagesConfig = testSessionDetails.agentTestId.flatMap(getTestStages).getOrElse(getDefaultTestStages)
        )
      }
  }

  private def getSessionEventKey(implicit testSessionLifecycleEvent: TestSessionLifecycleEvent): String = {
    s"${redisEntityPrefixes.sessionLifecyclePrefix}_${testSessionLifecycleEvent.testSession.testSessionId}"
  }
}

object RedisRepository {

  def apply(host: String, port: Int, timeout: Int, password: String, redisEntityPrefixes: RedisEntityPrefixes): RedisRepository = {
    val jedis = new Jedis(host, port, timeout)
    if (password.nonEmpty) {
      jedis.auth(password)
    }
    new RedisRepository(jedis, redisEntityPrefixes)
  }

  def apply(redisConfig: RedisConfig): RedisRepository = apply(redisConfig.host, redisConfig.port, redisConfig.port,
    redisConfig.password, redisConfig.redisEntityPrefixes)
}