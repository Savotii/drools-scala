package com.spirent.converter

import com.spirent.dto.rules.RuleContent

/**
 * @author ysavi2
 * @since 14.12.2021
 */
trait RuleContentConverter {
  def convertFromString(content: String): RuleContent

  def convertToString(content: RuleContent): String

  def clearSpecialSymbols(content: String): String
}
