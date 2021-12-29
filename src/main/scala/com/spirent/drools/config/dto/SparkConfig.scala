package com.spirent.drools.config.dto

case class SparkConfig(applicationName: String, batchDuration: Int, partitionsNumber: Int, localMode: Boolean)
