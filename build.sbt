name := "hacker-news"

version := "0.1"

scalaVersion := "2.13.3"

val akkaHttpVersion = "10.1.12"
val akkaVersion = "2.5.30"

enablePlugins(JavaAppPackaging)

libraryDependencies ++= Seq("com.typesafe.akka" %% "akka-http" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-stream" % akkaVersion,
  "com.typesafe.akka" %% "akka-http-core" % akkaHttpVersion,
  "com.typesafe.akka" %% "akka-http-spray-json" % akkaHttpVersion,
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.2",
  "org.mongodb.scala" %% "mongo-scala-driver" % "4.1.1",
  "ch.qos.logback" % "logback-classic" % "1.2.3",
  "org.scalatest" %% "scalatest" % "3.2.2" % Test,
  ("org.mockito" %% "mockito-scala" % "1.14.2" exclude("org.scalactic", "*")) % Test,
  "com.typesafe.akka" %% "akka-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-stream-testkit" % akkaVersion % Test,
  "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpVersion % Test)