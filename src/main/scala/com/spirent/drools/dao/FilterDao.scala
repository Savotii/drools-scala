package com.spirent.drools.dao

import com.spirent.drools.model.kpi.filter.KpiThresholdFilterModel

import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 23.12.2021
 */
trait FilterDao {
  def findAll: ListBuffer[KpiThresholdFilterModel]
}
