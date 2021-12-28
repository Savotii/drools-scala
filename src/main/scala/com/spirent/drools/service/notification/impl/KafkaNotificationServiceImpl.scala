package com.spirent.drools.service.notification.impl

import com.spirent.drools.config.kafka.Producer
import com.spirent.drools.service.notification.NotificationService

/**
 * @author ysavi2
 * @since 23.12.2021
 */
object KafkaNotificationServiceImpl extends NotificationService {
  override def sendAlertNotification(obj: String): Unit = {
    Producer.sendAlertNotification(obj)
  }
}
