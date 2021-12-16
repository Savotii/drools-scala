package com.spirent.drools.model

import javax.persistence._

/**
 * @author ysavi2
 * @since 15.12.2021
 */
@Entity
@Table(name = "locations")
class LocationModel {
  @Id
  @GeneratedValue
  var id: Long = _
  @Column
  var code: String = _
}
