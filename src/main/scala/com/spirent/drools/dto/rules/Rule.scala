package com.spirent.drools.dto.rules

/**
 * @author ysavi2
 * @since 14.12.2021
 */
case class Rule(id:Long, name:String, ruleContent:RuleContent, version:String, lastModified:String, creationTime:String)
