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
      |SELECT * FROM public.kpi_threshold
    """.stripMargin

  override def findAll: ListBuffer[KpiThresholdFilterModel] = {
    val result: ListBuffer[KpiThresholdFilterModel] = new ListBuffer[KpiThresholdFilterModel]()
    Class.forName(SqlConfig.driver)
    val connection = DriverManager.getConnection(SqlConfig.url, SqlConfig.username, SqlConfig.password)
    val statement = connection.createStatement()
    val resultSet = statement.executeQuery(selectAllThresholdFilters)


    while (resultSet.next()) {
      result.addOne(KpiThresholdFilterModel(
        resultSet.getLong("id"),
        resultSet.getString("test_id"),
        resultSet.getString("overlay_id"),
        resultSet.getLong("value")))
    }
    statement.close()
    connection.close()
    result
  }
}
