package com.spirent.drools.dto.rules

import org.drools.modelcompiler.builder.generator.RuleContext

import scala.collection.mutable
import scala.collection.mutable._

/**
 * @author ysavi2
 * @since 14.12.2021
 */
class RuleContent {
  private var _headerPackage: String = ""
  private var _imports: mutable.Set[String] = mutable.Set.empty
  private var _globalVariables: ListBuffer[String] = ListBuffer.empty
  private var _attributes: mutable.Set[String] = mutable.Set.empty
  private var _dialect: RuleContext.RuleDialect = RuleContext.RuleDialect.JAVA
  private var _clauses: ListBuffer[RuleClause] = ListBuffer.empty

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

  def getGlobalVariables: ListBuffer[String] = _globalVariables

  def setGlobalVariables(globalVariables: ListBuffer[String]): Unit = _globalVariables = globalVariables

  def setGlobalVariable(variable: String): Unit = _globalVariables.addOne(variable)

  def getAttributes: mutable.Set[String] = _attributes

  def setAttributes(attributes: mutable.Set[String]): Unit = _attributes = attributes

  def setAttribute(attribute: String): Unit = _attributes.add(attribute)

  def getDialect: RuleContext.RuleDialect = _dialect

  def setDialect(dialect: RuleContext.RuleDialect): Unit = _dialect = dialect

  def getClauses: ListBuffer[RuleClause] = _clauses

  def setClauses(clauses: ListBuffer[RuleClause]): Unit = _clauses = clauses

  def setClause(clause: RuleClause): Unit = _clauses.addOne(clause)

  override def toString: String = s"RuleContent(headerPakages=${_headerPackage}, imports=${_imports}, globalVariables=${_globalVariables}, attributes=${_attributes}," +
    s"dialect=${_dialect}, clauses=${_clauses}"
}
