package com.spirent.drools.service.notification

/**
 * @author ysavi2
 * @since 22.12.2021
 */
trait NotificationService {
  def send(obj: AnyRef): Unit
}
