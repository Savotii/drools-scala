package com.spirent.drools

import com.spirent.drools.dao.impl.RuleDaoImpl
import com.spirent.drools.dto.rules.global.GlobalRuleCounter
import com.spirent.drools.service.rule.impl.RuleServiceImpl

/**
 * @author ysavi2
 * @since 14.12.2021
 */
object RulesEngineStarter {

  def main(args: Array[String]): Unit = {
    val key = "Test"
    println("1. " + GlobalRuleCounter.get(key))
    println("2. " + GlobalRuleCounter.update(key))
    println("3. " + GlobalRuleCounter.get(key))
    println("4. " + GlobalRuleCounter.updateAndGet(key))
    println("5. " + GlobalRuleCounter.get(key))

    RuleServiceImpl.reloadRules
    RuleDaoImpl.findAllRules.foreach(m => println(m.toString))
  }

}
