package com.spirent.drools.dto.kpi

/**
 * @author ysavi2
 * @since 14.12.2021
 */
class Kpi {
  private var latency: Double = _
  private var sessionId: String = _
  private var phase: KpiPhase.phase = _
  private var location: Location = _
  private var failed: Boolean = false
  private var timestamp: Long = _
  private var comment: String = _

  def getSessionId: String = sessionId

  def getLatency: Double = latency

  def getPhase: KpiPhase.phase = phase

  def getLocation: Location = location

  def getFailed: Boolean = failed

  def getTimestamp: Long = timestamp

  def getComment: String = comment

  def setComment(com: String): Unit = comment = com

  def setIsFailed(flag: Boolean): Unit = failed = flag

  def setFailed(flag: Boolean): Unit = failed = failed
}