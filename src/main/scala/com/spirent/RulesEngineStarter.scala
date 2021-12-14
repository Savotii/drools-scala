package com.spirent

import com.spirent.dto.kpi.{Kpi, KpiPhase, Location}
import com.spirent.dto.rules.global.{GlobalBoolean, GlobalRuleCounter}

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

  }

}
