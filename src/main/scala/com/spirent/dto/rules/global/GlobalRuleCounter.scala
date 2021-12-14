package com.spirent.dto.rules.global

/**
 * @author ysavi2
 * @since 14.12.2021
 */
object GlobalRuleCounter {
  private var counter = scala.collection.mutable.Map[String, Integer]()

  def get(key: String): Integer = {
    counter.getOrElse(key, 0)
  }

  def update(key: String): Integer = {
    val holder: Option[Integer] = counter.get(key)
    var data: Int = if (holder.isEmpty) {
      0
    } else {
      holder.get
    }
    data += 1
    counter += key -> data
    data
  }

  def checkClause(key: String): Boolean = {
    updateAndGet(key) % 2 == 0
  }

  def updateAndGet(key: String): Integer = {
    val holder: Option[Integer] = counter.get(key)
    var data: Integer = holder.get
    data += 1
    counter += key -> data
    data
  }
}
