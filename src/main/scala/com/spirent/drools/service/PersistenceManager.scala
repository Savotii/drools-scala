package com.spirent.drools.service

import com.spirent.drools.config.Constants
import com.spirent.drools.dto.events.{NotValidEvent, TestSessionLifecycleEvent, TestSessionPersistenceEvent}

import java.util.UUID

object PersistenceManager {

  def getSessionPersistenceEvent(validEvent: Either[NotValidEvent, TestSessionLifecycleEvent]): Either[NotValidEvent, TestSessionPersistenceEvent] = {

    validEvent.map { testSessionLifecycleEvent =>

      val eventTimestamp = testSessionLifecycleEvent.eventType match {
        case Constants.SessionCreatedEvent => testSessionLifecycleEvent.testSession.createTime
        case Constants.SessionStartedEvent => testSessionLifecycleEvent.testSession.startTime.orNull
        case Constants.SessionCompletedEvent => testSessionLifecycleEvent.testSession.endTime.orNull
        case Constants.SessionErrorEvent => testSessionLifecycleEvent.testSession.endTime.orNull
      }

      TestSessionPersistenceEvent(
        id = UUID.randomUUID().toString,
        agentId = testSessionLifecycleEvent.testSession.agentId,
        agentTestName = testSessionLifecycleEvent.testSession.agentTestName,
        testSessionId = testSessionLifecycleEvent.testSession.testSessionId,
        testId = testSessionLifecycleEvent.testSession.testId,
        eventType = testSessionLifecycleEvent.eventType,
        agentTestId = testSessionLifecycleEvent.testSession.agentTestId,
        workflowId = testSessionLifecycleEvent.testSession.workflowId,
        overlayId = testSessionLifecycleEvent.testSession.overlayId,
        testCategory = testSessionLifecycleEvent.testSession.category,
        testPackage = testSessionLifecycleEvent.testSession.testPackage,
        testName = testSessionLifecycleEvent.testSession.testName,
        status = testSessionLifecycleEvent.testSession.status,
        eventTimestamp = eventTimestamp
      )
    }
  }
}
