package com.spirent.drools.dto.rules.global

/**
 * @author ysavi2
 * @since 14.12.2021
 */
object GlobalBoolean {
  var passed: Boolean = true

  def setIsPassed(flag: Boolean): Unit = passed = flag

  def isPassed : Boolean = passed
}
