package com.spirent.drools.dao

import com.spirent.drools.model.alert.{AlertModel, FailedKpiModel}

import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 23.12.2021
 */
trait AlertDao {
  def saveAlertModel(model: AlertModel): AlertModel

  def saveFailedKpis(kpis: ListBuffer[FailedKpiModel]): ListBuffer[FailedKpiModel]
}
