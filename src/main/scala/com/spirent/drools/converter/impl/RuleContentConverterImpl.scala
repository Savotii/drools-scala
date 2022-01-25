package com.spirent.drools.converter.impl

import com.spirent.drools.converter.RuleContentConverter
import com.spirent.drools.dto.rules.{RuleClause, RuleContent}
import lombok.extern.slf4j.Slf4j
import org.apache.commons.lang3.StringUtils
import org.drools.compiler.lang.DroolsSoftKeywords._
import org.drools.modelcompiler.builder.generator.RuleContext
import org.slf4j.{Logger, LoggerFactory}

import scala.collection.mutable
import scala.collection.mutable.ListBuffer

/**
 * @author ysavi2
 * @since 14.12.2021
 */
@Slf4j
object RuleContentConverterImpl extends RuleContentConverter {
  private val log: Logger = LoggerFactory.getLogger(this.getClass);
  private val LINE_SEPARATOR_SYMBOL: String = "_/_/"
  private val BREAK_LINE_SYMBOL = " \n "
  private val QUOTE = "\""
  private val SLASH = "\\"

  override def convertFromString(content: String): RuleContent = {
    if (content.isBlank) {
      new RuleContent
    }

    var convertedContent = new RuleContent
    var clauses: Map[String, RuleClause] = Map[String, RuleClause]()
    if (!content.contains(LINE_SEPARATOR_SYMBOL)) { //todo should be thought the approach of the deserializing.
      return convertedContent
    }

    var previousRuleName: String = StringUtils.EMPTY
    val splitted: Array[String] = content.split(LINE_SEPARATOR_SYMBOL)
    for (i <- 0 until splitted.length - 1) {
      val block: String = clearFirstSpecialSymbols(splitted(i))
      val splittedBlock: Array[String] = block.split(StringUtils.SPACE)
      val blockType: String = splittedBlock(0)
      blockType match {
        case PACKAGE => convertAndSetPackageStringToContent(convertedContent, block)
        case IMPORT => convertAndSetImportStringToContent(convertedContent, block)
        case GLOBAL => convertAndSetGlobalVariablesStringToContent(convertedContent, block)
        case ATTRIBUTES => convertAndSetAttributesStringToContent(convertedContent, block)
        case DIALECT => convertAndSetDialectStringToContent(convertedContent, block)
        case RULE => previousRuleName = getRuleNameFromPattern(block)
          val clause: RuleClause = clauses.getOrElse(previousRuleName, new RuleClause)
          clause.ruleName = previousRuleName
          clauses += (previousRuleName -> clause)

        case SALIENCE => convertAndSetSalienceFromStringToContent(clauses(previousRuleName), block)
        case WHEN =>
          val whenClause = clauses.get(previousRuleName)
          if (whenClause.isDefined) {
            convertAndSetWhenStringToContent(whenClause.get, block)
          }

        case THEN =>
          val thenClause = clauses.get(previousRuleName)
          if (thenClause.isDefined) {
            convertAndSetThenStringToContent(thenClause.get, block)
          }

        case _ => //log.info("Nothing to convert yet.")
      }
    }

    val res: ListBuffer[RuleClause] = new ListBuffer[RuleClause]()
    clauses.values.foreach(r => res+=r)
//    res += clauses.values.seq
//    convertedContent.setClauses(ListBuffer.from(clauses.values))
    convertedContent.setClauses(res)
    convertedContent
  }

  private def clearFirstSpecialSymbols(s: String) = s.replace(BREAK_LINE_SYMBOL, StringUtils.EMPTY)

  //todo build some validation of the input var
  override def convertToString(content: RuleContent): String = {
    val sb = new StringBuilder
    fillPackage(sb, content.getHeaderPackage)
    fillImports(sb, content.getImport)
    fillGlobalVariables(sb, content.getGlobalVariables)
    fillAttributes(sb, content.getAttributes)
    fillDialect(sb, content.getDialect)
    fillClauses(sb, content.getClauses)
    sb.toString
  }

  override def clearSpecialSymbols(content: String): String = content.replace(LINE_SEPARATOR_SYMBOL, StringUtils.SPACE).replace(QUOTE + QUOTE, QUOTE)

  // todo find out is this block is mandatory?
  private def fillPackage(sb: StringBuilder, rulePackage: String): Unit = {
    if (rulePackage.isBlank) return
    //todo should be validated.
    sb.append(PACKAGE).append(StringUtils.SPACE).append(rulePackage).append(LINE_SEPARATOR_SYMBOL).append(BREAK_LINE_SYMBOL)
  }

  private def convertAndSetPackageStringToContent(convertedContent: RuleContent, value: String): Unit = {
    convertedContent.setHeaderPackage(value.replace(PACKAGE, StringUtils.EMPTY).trim)
  }

  private def convertAndSetGlobalVariablesStringToContent(convertedContent: RuleContent, value: String): Unit = {
    convertedContent.setGlobalVariable(value.replace(GLOBAL.concat(StringUtils.SPACE), StringUtils.EMPTY).trim)
  }

  private def convertAndSetImportStringToContent(convertedContent: RuleContent, value: String): Unit = {
    convertedContent.setImport(value.replace(IMPORT, StringUtils.EMPTY).trim)
  }

  private def convertAndSetAttributesStringToContent(convertedContent: RuleContent, value: String): Unit = {
    convertedContent.setAttribute(value.replace(ATTRIBUTES, StringUtils.EMPTY).trim)
  }

  private def convertAndSetDialectStringToContent(convertedContent: RuleContent, value: String): Unit = {
    convertedContent.setDialect(RuleContext.RuleDialect.valueOf(value.replace(DIALECT, StringUtils.EMPTY).replace(QUOTE, StringUtils.EMPTY).trim.toUpperCase))
  }

  private def getRuleNameFromPattern(value: String) = value.replace(RULE, StringUtils.EMPTY).replace(QUOTE, StringUtils.EMPTY).trim

  private def convertAndSetSalienceFromStringToContent(clause: RuleClause, value: String): Unit = {
    if (value == null) return
    val res = value.replace(SALIENCE, StringUtils.EMPTY).trim
    try clause.salience = res.toLong
    catch {
      case e: Exception =>
        log.error("Salience conversion is failed", e)
    }
  }

  private def convertAndSetWhenStringToContent(clause: RuleClause, value: String): Unit = {
    clause.whenClause = value.replace(WHEN, StringUtils.EMPTY).trim
  }

  private def convertAndSetThenStringToContent(clause: RuleClause, value: String): Unit = {
    clause.thenClause = value.replace(THEN, StringUtils.EMPTY).trim
  }

  private def fillImports(sb: StringBuilder, ruleImports: mutable.Set[String]): Unit = {
    if (ruleImports.isEmpty) return

    ruleImports.foreach(i => sb.append(IMPORT).append(StringUtils.SPACE).append(i).append(LINE_SEPARATOR_SYMBOL).append(BREAK_LINE_SYMBOL))
  }

  private def fillGlobalVariables(sb: StringBuilder, globalVariables: ListBuffer[String]): Unit = {
    if (globalVariables.isEmpty) return
    globalVariables.foreach((i: String) => sb.append(GLOBAL).append(StringUtils.SPACE).append(i).append(LINE_SEPARATOR_SYMBOL).append(BREAK_LINE_SYMBOL))
  }

  //todo
  private def fillAttributes(sb: StringBuilder, ruleAttributes: mutable.Set[String]): Unit = {
    //stub
  }

  private def fillDialect(sb: StringBuilder, dialect: RuleContext.RuleDialect): Unit = {
    if (dialect == null) return
    sb.append(DIALECT).append(StringUtils.SPACE).append(wrapByQuotes(dialect.name.toLowerCase)).append(LINE_SEPARATOR_SYMBOL).append(BREAK_LINE_SYMBOL)
  }

  private def fillClauses(sb: StringBuilder, clauses: ListBuffer[RuleClause]): Unit = {
    if (clauses.isEmpty) return
    /* Example.
             * rule "Name of your rule"
             * when
             *   instance_of_the_class : Class(filed == "something")
             *   another_instance_of_the_class : AnotherClass();
             * then
             *   instance_of_the_class.setSomething(true/false/other values);
             *   another_instance_of_the_class.setSomething(true/false/other values);
             * end
             */
    clauses.foreach((cl: RuleClause) => {
      def foo(cl: RuleClause): Unit = {
        fillRuleName(sb, cl.ruleName)
        fillSalience(sb, Option(cl.salience))
        fillWhenClause(sb, cl.whenClause)
        fillThenClause(sb, cl.thenClause)
        fillEndBlock(sb)
      }

      foo(cl)
    })
  }

  private def fillRuleName(sb: StringBuilder, ruleName: String): Unit = {
    if (ruleName.isBlank) return
    sb.append(BREAK_LINE_SYMBOL).append(RULE).append(StringUtils.SPACE).append(wrapByQuotes(ruleName)).append(LINE_SEPARATOR_SYMBOL).append(BREAK_LINE_SYMBOL)
  }

  private def wrapByQuotes(ruleName: String) = QUOTE + ruleName + QUOTE

  private def fillSalience(sb: StringBuilder, salience: Option[Long]): Unit = {
    salience.getOrElse((sal: Long) => sb.append(SALIENCE).append(StringUtils.SPACE).append(sal).append(LINE_SEPARATOR_SYMBOL).append(BREAK_LINE_SYMBOL))
  }

  private def fillWhenClause(sb: StringBuilder, whenClause: String): Unit = {
    sb.append(WHEN).append(StringUtils.SPACE).append(whenClause).append(LINE_SEPARATOR_SYMBOL).append(BREAK_LINE_SYMBOL)
  }

  private def fillThenClause(sb: StringBuilder, thenClause: String): Unit = {
    sb.append(THEN).append(StringUtils.SPACE).append(thenClause).append(LINE_SEPARATOR_SYMBOL).append(BREAK_LINE_SYMBOL)
  }

  private def fillEndBlock(sb: StringBuilder): Unit = {
    sb.append(END).append(StringUtils.SPACE).append(LINE_SEPARATOR_SYMBOL).append(BREAK_LINE_SYMBOL)
  }
}
