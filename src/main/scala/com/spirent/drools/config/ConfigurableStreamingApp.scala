package com.spirent.drools.config

import com.spirent.drools.config.dto.{ApplicationConfiguration, KafkaConfig, RedisConfig, SparkConfig}
import wvlet.airframe.config.Config

trait ConfigurableStreamingApp {
  def loadConfiguration(applicationParameters: ApplicationParameters): ApplicationConfiguration = {
    val config: Config = Config(env = applicationParameters.environment, configPaths = Seq(applicationParameters.configPath))
      .registerFromYaml[KafkaConfig]("config/kafka-config.yml")
      .registerFromYaml[SparkConfig]("config/spark-config.yml")
      .registerFromYaml[RedisConfig]("config/redis-config.yml")

    val kafkaConfig = config.of[KafkaConfig]
    val sparkConfig = config.of[SparkConfig]
    val redisConfig = config.of[RedisConfig]

    val overloadedRedisConfig = redisConfig.copy(
      host = applicationParameters.redisHost.getOrElse(redisConfig.host),
      port = applicationParameters.redisPort.getOrElse(redisConfig.port),
      timeout = applicationParameters.redisPort.getOrElse(redisConfig.timeout),
      password = applicationParameters.redisPassword.getOrElse(redisConfig.password),
    )

    val overloadedKafkaConfig = kafkaConfig.copy(
      bootstrapServers = applicationParameters.kafkaBootstrapServers.getOrElse(kafkaConfig.bootstrapServers),
      offsetReset = applicationParameters.kafkaOffsetReset.getOrElse(kafkaConfig.offsetReset)
    )

    val overloadedSparkConfig = sparkConfig.copy(
      partitionsNumber = applicationParameters.sparkPartitionsNumber.getOrElse(sparkConfig.partitionsNumber),
      batchDuration = applicationParameters.sparkBatchDuration.getOrElse(sparkConfig.batchDuration)
    )

    ApplicationConfiguration(
      kafkaConfig = overloadedKafkaConfig,
      sparkConfig = overloadedSparkConfig,
      redisConfig = overloadedRedisConfig
    )
  }
}