package com.spirent.drools.dao.impl

import com.spirent.drools.converter.impl.RuleContentConverterImpl
import com.spirent.drools.dao.RuleDao
import com.spirent.drools.dto.rules.Rule
import com.spirent.drools.model.rule.RuleModel
import com.spirent.drools.sql.ConnectionUtil

import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 16.12.2021
 */
object RuleDaoImpl extends RuleDao {
  override def findAllRules: ListBuffer[Rule] = {
    val result = ConnectionUtil.selectRulesFromDb("Select * from rules")
    ListBuffer.from(result.map(mapToRule))
  }

  def mapToRule(model: RuleModel): Rule = {
    Rule(model.id, model.ruleKey, RuleContentConverterImpl.convertFromString(model.content), model.version, model.lastModifyTime, model.createTime)
  }
}
