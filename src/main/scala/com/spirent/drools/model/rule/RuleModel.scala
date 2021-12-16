package com.spirent.drools.model.rule

import javax.persistence._

/**
 * @author ysavi2
 * @since 15.12.2021
 */
@Entity
@Table(name = "rules")
class RuleModel {

  @Id
  @GeneratedValue
  var id: Long = _
  @Column(nullable = false, unique = true)
  var ruleKey: String = _
  @Column(nullable = false)
  var content: String = _
  @Column(nullable = false, unique = true)
  var version: String = _
  @Column(unique = true)
  var lastModifyTime: String = _
  @Column(nullable = false)
  var createTime: String = _

}
