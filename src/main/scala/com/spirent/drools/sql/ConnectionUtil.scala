package com.spirent.drools.sql

import com.spirent.drools.model.rule.RuleModel

import java.sql.DriverManager
import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 15.12.2021
 */
object ConnectionUtil {
  def selectRulesFromDb(query: String): ListBuffer[RuleModel] = {
    val rules: ListBuffer[RuleModel] = ListBuffer[RuleModel]()
    try {
      Class.forName(SqlConfig.driver)
      val connection = DriverManager.getConnection(SqlConfig.url, SqlConfig.username, SqlConfig.password)
      val statement = connection.createStatement()
      val resultSet = statement.executeQuery(query)

      while (resultSet.next()) {
        val model = new RuleModel()
        model.id = resultSet.getLong("id")
        model.ruleKey = resultSet.getString("rule_key")
        model.content = resultSet.getString("content")
        model.version = resultSet.getString("version")
        model.lastModifyTime = resultSet.getString("last_modify_time")
        model.createTime = resultSet.getString("create_time")
        rules.addOne(model)
      }
    } catch {
      case e: Throwable => e.printStackTrace
    }
    rules
  }
}
