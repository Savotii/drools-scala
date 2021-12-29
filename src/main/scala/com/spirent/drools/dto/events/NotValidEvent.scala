package com.spirent.drools.dto.events

case class NotValidEvent(sourceJson: String, errorDescription: Map[String, Object])