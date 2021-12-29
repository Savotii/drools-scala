package com.spirent.drools.config

import com.spirent.drools.config.dto.{ApplicationConfiguration, KafkaConfig, KafkaTopics, SparkConfig}
import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession
import org.apache.spark.streaming.{Seconds, StreamingContext}

trait SparkComponent {

  def initSparkStreamingContext(sparkConfig: SparkConfig): StreamingContext = {

    val spark = SparkSession.builder()
      .config(getSparkConfiguration(sparkConfig))
      .getOrCreate()

    new StreamingContext(spark.sparkContext, Seconds(sparkConfig.batchDuration))
  }

  def getSparkConfiguration(sparkConfig: SparkConfig): SparkConf = {
    if (sparkConfig.localMode)
      getLocalSparkConfig(sparkConfig)
    else
      getClusterSparkConfig(sparkConfig)
  }

  private def getLocalSparkConfig(sparkConfig: SparkConfig): SparkConf = {
    new SparkConf(true)
      .setAppName(sparkConfig.applicationName)
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .registerKryoClasses(getKryoClasses)
      .setMaster("local[3]")
      .set("spark.driver.bindAddress", "127.0.0.1")
  }

  private def getClusterSparkConfig(sparkConfig: SparkConfig): SparkConf = {
    new SparkConf(true)
      .setAppName(sparkConfig.applicationName)
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .registerKryoClasses(getKryoClasses)
  }

  private def getKryoClasses = {
    Array[Class[_]](
      classOf[ApplicationConfiguration],
      classOf[KafkaConfig],
      classOf[KafkaTopics],
      classOf[SparkConfig],
//      classOf[ValidEvent],
//      classOf[NotValidEvent],
//      classOf[TestSessionLifecycleEvent],
//      classOf[TestSessionCreatedTestParameters],
//      classOf[TestSessionLifecycleEventDetails],
//      classOf[TestSessionDto],
//      classOf[TestSessionPersistenceEvent],
//      classOf[AgentTestStagesConfig],
//      classOf[TestStage],
//      classOf[TestStageStatement],
    )
  }
}