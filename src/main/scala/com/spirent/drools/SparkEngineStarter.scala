package com.spirent.drools

import com.spirent.drools.config.dto.ApplicationConfiguration
import com.spirent.drools.config.kafka.KafkaComponent
import com.spirent.drools.config.{ApplicationParameters, ConfigurableStreamingApp, SparkComponent}
import com.spirent.drools.service.VtaSessionLifecycleStreaming
import com.spirent.drools.service.kpi.impl.KpiServiceImpl
import com.spirent.drools.service.ruleengine.impl.DroolsEngineServiceImpl
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.spark.streaming.StreamingContext
import org.apache.spark.streaming.dstream.InputDStream
import org.slf4j.{Logger, LoggerFactory}


/**
 * @author ysavi2
 * @since 28.12.2021
 */
object SparkEngineStarter extends ConfigurableStreamingApp with KafkaComponent with SparkComponent {
  private val log: Logger = LoggerFactory.getLogger(this.getClass)

  def main(args: Array[String]): Unit = {
    val applicationParameters = parseArgs(args)
      .getOrElse {
        val errorMessage = s"Provided application parameters not valid! Received args: ${args.mkString("Array(", ", ", ")")}"
        log.error(errorMessage)
        throw new IllegalArgumentException(errorMessage)
      }

    //1. get all thresholds etc.
    KpiServiceImpl.updateFilters()
    //2. load the drools context
    DroolsEngineServiceImpl.rebuildWholeContext()

    val applicationConfiguration: ApplicationConfiguration = loadConfiguration(applicationParameters)
    val ssc: StreamingContext = initSparkStreamingContext(applicationConfiguration.sparkConfig)
    val kafkaInputStream: InputDStream[ConsumerRecord[String, String]] = getKafkaInputStream(ssc, applicationConfiguration)

    VtaSessionLifecycleStreaming.runStreaming(applicationConfiguration, ssc, kafkaInputStream)
  }

  private def parseArgs(args: Array[String]): Option[ApplicationParameters] = {
    //    CommandLineParser
    val parser = new scopt.OptionParser[ApplicationParameters]("scopt") {
      head("scopt", "3.x")

      opt[String]("environment").required().action { (x, c) =>
        c.copy(environment = x)
      }
      opt[String]("executors").optional().action { (x, c) =>
        c.copy(executors = x.toInt)
      }
      opt[String]("config-path").optional().action { (x, c) =>
        c.copy(configPath = x)
      }
      opt[String]("kafka-bootstrap-servers").optional().action { (x, c) =>
        c.copy(kafkaBootstrapServers = Option(x))
      }
      opt[String]("kafka-offset-reset").optional().action { (x, c) =>
        c.copy(kafkaOffsetReset = Option(x))
      }
      opt[String]("redis-host").optional().action { (x, c) =>
        c.copy(redisHost = Option(x))
      }
      opt[String]("redis-port").optional().action { (x, c) =>
        c.copy(redisPort = Option(x.toInt))
      }
      opt[String]("redis-timeout").optional().action { (x, c) =>
        c.copy(redisTimeout = Option(x.toInt))
      }
      opt[String]("redis-password").optional().action { (x, c) =>
        c.copy(redisPassword = Option(x))
      }
      opt[String]("spark-batch-duration").optional().action { (x, c) =>
        c.copy(sparkBatchDuration = Option(x.toInt))
      }
      opt[String]("spark-partitions-number").optional().action { (x, c) =>
        c.copy(sparkPartitionsNumber = Option(x.toInt))
      }
    }

    parser.parse(args, ApplicationParameters())
  }

}
