package com.spirent.drools.config

case class ApplicationParameters(environment: String = "local",
                                 executors: Int = 4,
                                 configPath: String = "./src/main/resources/config",
                                 kafkaBootstrapServers: Option[String] = None,
                                 kafkaOffsetReset: Option[String] = None,
                                 redisHost: Option[String] = None,
                                 redisPort: Option[Int] = None,
                                 redisTimeout: Option[Int] = None,
                                 redisPassword: Option[String] = None,
                                 sparkBatchDuration: Option[Int] = None,
                                 sparkPartitionsNumber: Option[Int] = None)