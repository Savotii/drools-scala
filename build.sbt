name := "Spark-drools"

version := "1.0"

scalaVersion := "2.12.15" //"2.13.7" //

val sparkVersion = "3.2.0"
val kafkaVersion = "2.8.0"

resolvers ++= Seq(
  "apache-snapshots" at "http://repository.apache.org/snapshots/",
  "Typesafe Simple Repository" at "https://repo.typesafe.com/typesafe/simple/maven-releases",
  "MavenRepository" at "https://mvnrepository.com",
  "MavenCentral" at "https://repo1.maven.org/maven2/"
)

val droolsVersion = "7.62.0.Final"
val mvelVersion = "2.4.13.Final"
val airframeVersion = "21.10.0"
val jedisVersion = "3.6.3"
val scoptVersion = "4.0.1"
val kollflitzVersion = "0.2.4"

libraryDependencies += "org.drools" % "drools-compiler" % droolsVersion
libraryDependencies += "org.drools" % "drools-model-compiler" % droolsVersion
libraryDependencies += "org.drools" % "drools-core" % droolsVersion
libraryDependencies += "org.kie" % "kie-api" % droolsVersion
libraryDependencies += "org.kie" % "kie-internal" % droolsVersion
libraryDependencies += "org.jbpm" % "jbpm-test" % droolsVersion
libraryDependencies += "org.drools" % "drools-mvel" % "7.62.0.Final"
libraryDependencies += "org.drools" % "drools-core-reflective" % "7.62.0.Final"
libraryDependencies += "org.drools" % "drools-core-dynamic" % "7.62.0.Final"
libraryDependencies += "org.drools" % "drools-decisiontables" % "7.62.0.Final"
libraryDependencies += "org.drools" % "drools-persistence-api" % "7.62.0.Final"
libraryDependencies += "org.jbpm" % "jbpm-persistence-api" % "7.62.0.Final"
libraryDependencies += "org.jbpm" % "jbpm-persistence-jpa" % "7.62.0.Final"
libraryDependencies += "io.swagger.core.v3" % "swagger-annotations" % "2.1.11"
libraryDependencies += "org.springdoc" % "springdoc-openapi-ui" % "1.5.11"
libraryDependencies += "javax.validation" % "validation-api" % "2.0.1.Final"
libraryDependencies += "ma.glasnost.orika" % "orika-core" % "1.5.4"
libraryDependencies += "org.projectlombok" % "lombok" % "1.18.22"
//todo without spark it worked.
libraryDependencies += "org.apache.kafka" %% "kafka" % "3.0.0"
libraryDependencies += "org.postgresql" % "postgresql" % "42.3.1"
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.3.3"
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.5"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.5"
//libraryDependencies += "io.bfil" %% "automapper" % "0.7.0"
libraryDependencies += "io.bfil" %% "automapper" % "0.6.2"
libraryDependencies += "org.apache.spark" %% "spark-core" % sparkVersion % "provided"
libraryDependencies += "org.apache.spark" %% "spark-sql" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-streaming" % sparkVersion
libraryDependencies += "org.apache.spark" %% "spark-streaming-kafka-0-10" % sparkVersion
// additional libs
libraryDependencies += "com.github.scopt" %% "scopt" % scoptVersion
libraryDependencies += "org.wvlet.airframe" %% "airframe-config" % airframeVersion
libraryDependencies += "de.sciss" %% "kollflitz" % kollflitzVersion
libraryDependencies +=
  // redis
  "redis.clients" % "jedis" % jedisVersion

Compile / mainClass := Some("com.spirent.drools.SparkEngineStarter")
assembly / mainClass := Some("com.spirent.drools.SparkEngineStarter")

// META-INF discarding
ThisBuild / assemblyMergeStrategy := {
  case PathList("META-INF", xs @ _*) => MergeStrategy.discard
  case x => MergeStrategy.first
}

