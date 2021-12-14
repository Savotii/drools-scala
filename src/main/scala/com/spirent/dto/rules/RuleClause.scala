package com.spirent.dto.rules

/**
 * @author ysavi2
 * @since 14.12.2021
 */
case class RuleClause(var ruleName:String = "",
                      var whenClause:String = "",
                      var thenClause:String = "")