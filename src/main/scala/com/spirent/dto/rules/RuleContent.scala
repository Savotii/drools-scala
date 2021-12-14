package com.spirent.dto.rules

import org.drools.modelcompiler.builder.generator.RuleContext

/**
 * @author ysavi2
 * @since 14.12.2021
 */
class RuleContent {
  private var _headerPackage: String = ""
  private var _imports: Set[String] = Set.empty
  private var _globalVariables: List[String] = List.empty
  private var _attributes: Set[String] = Set.empty
  private var _dialect: RuleContext.RuleDialect = RuleContext.RuleDialect.JAVA
  private var _clauses: List[RuleClause] = List.empty

  def getHeaderPackage: String = {
    _headerPackage
  }

  def setHeaderPackage(header: String): Unit = {
    _headerPackage = header
  }

  def getImport: Set[String] = {
    _imports
  }

  def setImports(imports: Set[String]): Unit = {
    _imports = imports
  }

  def setImport(impr: String): Unit = _imports += impr

  def getGlobalVariables: List[String] = _globalVariables

  def setGlobalVariables(globalVariables: List[String]): Unit = _globalVariables = globalVariables

  def setGlobalVariable(variable: String): Unit = _globalVariables += (variable)

  def getAttributes: Set[String] = _attributes

  def setAttributes(attributes: Set[String]): Unit = _attributes = attributes

  def setAttribute(attribute: String): Unit = _attributes += attribute

  def getDialect: RuleContext.RuleDialect = _dialect

  def setDialect(dialect: RuleContext.RuleDialect): Unit = _dialect = dialect

  def getClauses: List[RuleClause] = _clauses

  def setClauses(clauses: List[RuleClause]): Unit = _clauses = clauses
}
