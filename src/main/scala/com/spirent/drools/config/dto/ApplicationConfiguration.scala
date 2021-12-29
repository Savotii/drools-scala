package com.spirent.drools.config.dto

case class ApplicationConfiguration(kafkaConfig: KafkaConfig,
                                    sparkConfig: SparkConfig,
                                    redisConfig: RedisConfig)
