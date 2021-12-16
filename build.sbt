name := "Spark-drools"

version := "1.0"

scalaVersion := "2.13.7" //"2.12.7" //

val sparkVersion = "3.2.0"

resolvers ++= Seq(
  "apache-snapshots" at "http://repository.apache.org/snapshots/",
  "Typesafe Simple Repository" at "https://repo.typesafe.com/typesafe/simple/maven-releases",
  "MavenRepository" at "https://mvnrepository.com",
  "MavenCentral" at "https://repo1.maven.org/maven2/"
)

libraryDependencies ++= Seq(
  "org.apache.spark" %% "spark-core" % sparkVersion,
  "org.apache.spark" %% "spark-sql" % sparkVersion
)


val droolsVersion = "7.62.0.Final"
val mvelVersion = "2.4.13.Final"

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
libraryDependencies += "org.apache.kafka" %% "kafka" % "3.0.0"
//// https://mvnrepository.com/artifact/org.hibernate.orm/hibernate-core
//libraryDependencies += "org.hibernate.orm" % "hibernate-core" % "6.0.0.Beta2"
// https://mvnrepository.com/artifact/org.postgresql/postgresql
libraryDependencies += "org.postgresql" % "postgresql" % "42.3.1"
libraryDependencies += "com.typesafe.slick" %% "slick" % "3.3.3"
libraryDependencies += "org.slf4j" % "slf4j-api" % "1.7.5"
libraryDependencies += "org.slf4j" % "slf4j-simple" % "1.7.5"
