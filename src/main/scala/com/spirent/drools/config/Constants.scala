package com.spirent.drools.config

object Constants {

  val SchemaNotFound = "No schema defined!"
  val PropertiesNotDefined = "Properties not defined in registered schema!"
  val PropertiesHasNullValue = "Null values found!"
  val PropertiesHasWrongType = "Property has wrong type!"

  val HostSuffixes = Set("_1", "_2", "_3", "_4", "_5", "_6", "_7", "_8")

  val SessionCreatedEvent = "TEST_SESSION_CREATED"
  val SessionStartedEvent = "TEST_SESSION_STARTED"
  val SessionCompletedEvent = "TEST_SESSION_COMPLETED"
  val SessionErrorEvent = "TEST_SESSION_ERROR"

}