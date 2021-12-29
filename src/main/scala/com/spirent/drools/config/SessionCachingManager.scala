package com.spirent.drools.config

import com.spirent.drools.config.Constants.{SessionCompletedEvent, SessionCreatedEvent, SessionErrorEvent, SessionStartedEvent}
import com.spirent.drools.dto.events.{NotValidEvent, TestSessionLifecycleEvent}
import org.apache.log4j.LogManager

/**
 * @author ysavi2
 * @since 28.12.2021
 */
class SessionCachingManager(initRedisRepositorySink: () => RedisRepositorySink) extends Serializable {

  private[this] lazy val log = LogManager.getLogger(this.getClass)
  private[this] lazy val redisRepository = initRedisRepositorySink()

  def processSessionLifecycleEvent(maybeValidEvent: Either[NotValidEvent, TestSessionLifecycleEvent]): Either[NotValidEvent, TestSessionLifecycleEvent] = {
    maybeValidEvent match {
      case Left(_) => maybeValidEvent
      case Right(testSessionLifecycleEvent) =>

        val event = testSessionLifecycleEvent.eventType match {
          case SessionCreatedEvent => redisRepository.cacheSession(testSessionLifecycleEvent)
          case SessionStartedEvent => redisRepository.updateSessionStartTime(testSessionLifecycleEvent)
          case SessionCompletedEvent => redisRepository.updateSessionEndTime(testSessionLifecycleEvent)
          case SessionErrorEvent => testSessionLifecycleEvent
          case _ => log.error(s"Type of session: ${testSessionLifecycleEvent.eventType} not supported!")
            testSessionLifecycleEvent
        }

        Right(event)
    }
  }
}

object SessionCachingManager {
  def apply(initRedisRepositorySink: () => RedisRepositorySink): SessionCachingManager = new SessionCachingManager(initRedisRepositorySink)
}
