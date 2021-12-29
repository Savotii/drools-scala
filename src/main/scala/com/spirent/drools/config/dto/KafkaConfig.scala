package com.spirent.drools.config.dto

case class KafkaConfig(bootstrapServers: String,
                       offsetReset: String,
                       enableAutoCommit: Boolean,
                       consumerGroup: String,
                       topics: KafkaTopics)

object KafkaConfig {

  implicit class ReachKafkaConfig(kafkaConfig: KafkaConfig) extends Serializable {

    def getNotValidEventsTopic: String = {
      kafkaConfig.topics.destinations("notValidEvents")
    }

    def getToPersistenceEventsTopic: String = {
      kafkaConfig.topics.destinations("persistenceEvents")
    }

  }
}