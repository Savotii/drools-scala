package com.spirent.drools.dao

import com.spirent.drools.dto.rules.Rule

import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 16.12.2021
 */
trait RuleDao {
  def findAllRules : ListBuffer[Rule]
}
