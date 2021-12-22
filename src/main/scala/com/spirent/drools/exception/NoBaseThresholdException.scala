package com.spirent.drools.exception

/**
 * @author ysavi2
 * @since 22.12.2021
 */
final case class NoBaseThresholdException(private val message: String = "",
                                          private val cause: Throwable = None.orNull)
  extends Exception(message, cause)
