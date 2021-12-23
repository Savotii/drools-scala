package com.spirent.drools.dao.impl

import com.spirent.drools.dao.FilterDao
import com.spirent.drools.model.kpi.filter.KpiThresholdFilterModel
import com.spirent.drools.sql.SqlConfig

import java.sql.DriverManager
import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 23.12.2021
 */
object FilterRepositoryDao extends FilterDao {
  val selectAllThresholdFilters: String =
    """
      |INSERT INTO public.alerts (agent_id, alert_id, agent_test_name, test_session_id, test_id, agent_test_id,
      |workflow_id, overlay_id, network_element_id, category, "package", test_name, alert_name,
      |level, "timestamp")
      |VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
    """.stripMargin

  override def findAll: ListBuffer[KpiThresholdFilterModel] = {
    val result: ListBuffer[KpiThresholdFilterModel] = new ListBuffer[KpiThresholdFilterModel]()
    Class.forName(SqlConfig.driver)
    val connection = DriverManager.getConnection(SqlConfig.url, SqlConfig.username, SqlConfig.password)
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(selectAllThresholdFilters)
    statement.close()
    connection.close()

    while (resultSet.next()) {
      result.addOne(KpiThresholdFilterModel(
        resultSet.getLong("id"),
        resultSet.getString("test_id"),
        resultSet.getString("overlay_id"),
        resultSet.getLong("value")))
    }
    result
  }
}
