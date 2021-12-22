package com.spirent.drools.listeners

import org.drools.core.event.DefaultAgendaEventListener
import org.kie.api.definition.rule.Rule
import org.kie.api.event.rule.AfterMatchFiredEvent
import org.kie.api.runtime.rule.Match
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.JavaConverters.mapAsScalaMap
import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 22.12.2021
 */
object TrackingAgendaEventListener extends DefaultAgendaEventListener {
  private val log: Logger = LoggerFactory.getLogger(getClass)
  private val matchList: ListBuffer[Match] = new ListBuffer[Match]()

  def logEvent(event: AfterMatchFiredEvent): Unit = {
    val rule: Rule = event.getMatch.getRule

    val ruleName: String = rule.getName
    val ruleMetaDataMap: scala.collection.mutable.Map[String, AnyRef] = mapAsScalaMap(rule.getMetaData)
    val sb: StringBuilder = new StringBuilder("Rule fired: " + ruleName)

    if (ruleMetaDataMap.nonEmpty) {
      sb.append("\n  With [").append(ruleMetaDataMap.size).append("] meta-data:")
      for (key <- ruleMetaDataMap.keySet) {
        sb.append("\n    key=").append(key).append(", value=").append(ruleMetaDataMap.get(key))
      }
    }

    log.debug(sb.toString)
  }

  override def afterMatchFired(event: AfterMatchFiredEvent): Unit = {
    matchList.addOne(event.getMatch)
    logEvent(event)
  }

  def reset(): Unit = {
    matchList.clear()
  }

  def getMatchList: ListBuffer[Match] = matchList
}
