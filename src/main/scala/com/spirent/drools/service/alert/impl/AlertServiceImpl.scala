package com.spirent.drools.service.alert.impl

import com.spirent.drools.dao.impl.AlertRepositoryDao
import com.spirent.drools.dto.alert.AlertEvent
import com.spirent.drools.dto.kpi.{FailedKpi, KpiLatency}
import com.spirent.drools.model.alert.{AlertModel, FailedKpiModel}
import com.spirent.drools.service.alert.AlertService
import com.spirent.drools.service.notification.impl.KafkaNotificationServiceImpl
import org.kie.api.runtime.rule.Match

import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 23.12.2021
 */
object AlertServiceImpl extends AlertService {
  override def send(alert: AlertEvent): Unit = {
    KafkaNotificationServiceImpl.sendAlertNotification(alert.toJSON())
  }

  override def buildFailedKpiLatencyAlert(matches: ListBuffer[Match]): ListBuffer[FailedKpi] = {
    val result = new ListBuffer[FailedKpi]()
    for (found <- matches) {
      found.getObjects.forEach(kpl => {
        val fkpi = new FailedKpi
        fkpi.name = found.getRule.getName
        fkpi.latency = kpl.asInstanceOf[KpiLatency].latency
        fkpi.threshold = kpl.asInstanceOf[KpiLatency].threshold
        result.addOne(fkpi)
      })
    }
    result
  }

  override def saveEvent(alert: AlertEvent): Unit = {
    val model = mapToModel(alert)
    val savedModel = AlertRepositoryDao.saveAlertModel(model)
    model.failedKpis.foreach { fkpi =>
      fkpi.alertId = savedModel.id
    }
    AlertRepositoryDao.saveFailedKpis(model.failedKpis)
  }

  def mapToModel(value: AlertEvent): AlertModel = {
    val failedKpiModels = value.failedKpis.map(mapToFailedKpiModel)
    val model = new AlertModel()

    model.alertId = value.alertId
    model.agentId = value.agentId
    model.agentTestName = value.agentTestName
    model.testSessionId = value.testSessionId
    model.testId = value.testId
    model.agentTestId = value.agentTestId
    model.workflowId = value.workflowId
    model.overlayId = value.overlayId
    model.networkElementId = value.networkElementId
    model.category = value.category
    model.pkg = value.pkg
    model.testName = value.testName
    model.timestamp = value.timestamp
    model.level = value.level.toString
    model.alertName = value.name
    model.failedKpis = failedKpiModels
    model
  }


  def mapToFailedKpiModel(kpi: FailedKpi): FailedKpiModel = {
    val model = new FailedKpiModel
    model.latency = kpi.latency
    model.threshold = kpi.threshold
    model
  }
}
