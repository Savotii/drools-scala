package com.spirent.drools.dao.impl

import com.spirent.drools.dao.AlertDao
import com.spirent.drools.model.alert.{AlertModel, FailedKpiModel}
import com.spirent.drools.sql.SqlConfig

import java.sql.{DriverManager, PreparedStatement}
import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 23.12.2021
 */
object AlertRepositoryDao extends AlertDao {
  val insertAlertModelSql: String =
    """
      |INSERT INTO rules.alerts (agent_id, alert_id, agent_test_name, test_session_id, test_id, agent_test_id,
      |workflow_id, overlay_id, network_element_id, category, "package", test_name, alert_name,
      |level, "timestamp")
      |VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """.stripMargin
  val insertFailedKpisSql: String =
    """
      |INSERT INTO rules.failed_kpis(
      |latency, threshold, alert_id)
      |VALUES (?, ?, ?);
    """.stripMargin

  val selectAlertId: String =
    """
      |SELECT id FROM rules.alerts
      |WHERE "agent_id" = ? and "alert_id" = ? and "test_session_id" = ?
      |""".stripMargin
  override def saveAlertModel(model: AlertModel): AlertModel = {
    Class.forName(SqlConfig.driver)
    val connection = DriverManager.getConnection(SqlConfig.url, SqlConfig.username, SqlConfig.password)
    val preparedStmt: PreparedStatement = connection.prepareStatement(insertAlertModelSql)
    preparedStmt.setString(1, model.agentId)
    preparedStmt.setString(2, model.alertId)
    preparedStmt.setString(3, model.agentTestName)
    preparedStmt.setString(4, model.testSessionId)
    preparedStmt.setString(5, model.testId)
    preparedStmt.setString(6, model.agentTestId)
    preparedStmt.setString(7, model.workflowId)
    preparedStmt.setString(8, model.overlayId)
    preparedStmt.setString(9, model.networkElementId)
    preparedStmt.setString(10, model.category)
    preparedStmt.setString(11, model.pkg)
    preparedStmt.setString(12, model.testName)
    preparedStmt.setString(13, model.alertName)
    preparedStmt.setString(14, model.level)
    preparedStmt.setString(15, model.timestamp.toString)
    preparedStmt.execute()
    preparedStmt.close()

    //get the id of the saved model
    val statement = connection.prepareStatement(selectAlertId)
    statement.setString(1, model.agentId)
    statement.setString(2, model.alertId)
    statement.setString(3, model.testSessionId)
    val result = statement.executeQuery()
    if (result.next()){
      model.id = result.getLong(1)
    }
    statement.close()
    connection.close()
    //todo map to alertModel and return

    model

  }

  override def saveFailedKpis(kpis: ListBuffer[FailedKpiModel]): ListBuffer[FailedKpiModel] = {
    Class.forName(SqlConfig.driver)
    val connection = DriverManager.getConnection(SqlConfig.url, SqlConfig.username, SqlConfig.password)
    val preparedStmt: PreparedStatement = connection.prepareStatement(insertFailedKpisSql)

    kpis.foreach(kpi => {
      preparedStmt.setLong(1, kpi.latency)
      preparedStmt.setLong(2, kpi.threshold)
      preparedStmt.setLong(3, kpi.alertId)
      preparedStmt.addBatch()
    })
    preparedStmt.close()
    connection.close()

    kpis
  }
}
