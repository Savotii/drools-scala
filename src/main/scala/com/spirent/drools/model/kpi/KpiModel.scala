package com.spirent.drools.model.kpi

import com.spirent.drools.dto.kpi.KpiPhase
import com.spirent.drools.model.LocationModel
import lombok.{Getter, Setter}

import javax.persistence._

/**
 * @author ysavi2
 * @since 15.12.2021
 */
@Entity
@Table(name = "kpis")
class KpiModel {
  @Id
  @GeneratedValue
  var id: Long = _
  @Column
  var sessionId: String = _
  @Enumerated(EnumType.ORDINAL)
  @Column
  var phase: KpiPhase.phase = _
  @ManyToOne(cascade = Array{CascadeType.ALL} )
  @JoinColumn(name = "location_id")
  var location: LocationModel = _
  @Column
  var failed: Boolean = _
  @Column
  var timestamp: Long = _

}
