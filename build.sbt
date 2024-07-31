name := "pekko-grpc-quickstart-scala"

version := "1.0"

scalaVersion := "2.13.14"

lazy val pekkoVersion = "1.0.3"
lazy val pekkoGrpcVersion = "1.0.2"

enablePlugins(PekkoGrpcPlugin)

// Run in a separate JVM, to make sure sbt waits until all threads have
// finished before returning.
// If you want to keep the application running while executing other
// sbt tasks, consider https://github.com/spray/sbt-revolver/
fork := true

libraryDependencies ++= Seq(
  "org.apache.pekko" %% "pekko-actor-typed" % pekkoVersion,
  "org.apache.pekko" %% "pekko-stream" % pekkoVersion,
  "org.apache.pekko" %% "pekko-discovery" % pekkoVersion,
  "org.apache.pekko" %% "pekko-pki" % pekkoVersion,
  "ch.qos.logback" % "logback-classic" % "1.2.13",
  "org.apache.pekko" %% "pekko-actor-testkit-typed" % pekkoVersion % Test,
  "org.apache.pekko" %% "pekko-stream-testkit" % pekkoVersion % Test,
  "org.scalatest" %% "scalatest" % "3.2.19" % Test,
  "ch.qos.logback" % "logback-classic" % "1.2.11",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.9.5"
)
