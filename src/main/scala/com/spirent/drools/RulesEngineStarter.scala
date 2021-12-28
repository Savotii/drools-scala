package com.spirent.drools

import com.spirent.drools.config.kafka.{Consumer, Producer}
import com.spirent.drools.dao.impl.RuleDaoImpl
import com.spirent.drools.dto.rules.globals.GlobalRuleCounter
import com.spirent.drools.service.kpi.impl.KpiServiceImpl
import com.spirent.drools.service.rule.impl.RuleServiceImpl
import com.spirent.drools.service.ruleengine.impl.DroolsEngineServiceImpl

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

//    RuleServiceImpl.reloadRules
    //1. get all thresholds etc.
    KpiServiceImpl.updateFilters()
    //2. load the drools context
    DroolsEngineServiceImpl.rebuildWholeContext()
    Consumer.listen()
    Producer.send()
    RuleDaoImpl.findAllRules.foreach(m => println(m.toString))

    new Thread(() => while(true) {}).start()
  }

}
