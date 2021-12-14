package com.spirent.dto.kpi

/**
 * @author ysavi2
 * @since 14.12.2021
 */
class Kpi(latency:Double = 0.0, sessionId:String = "", phase: KpiPhase.phase,
          location: Location, failed: Boolean = false, timestamp: Long, comment: String ="")