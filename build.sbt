name := "spbf"

version := "0.1.0"

scalaVersion := "2.11.5"

scalacOptions ++= Seq("-feature")

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "org.scala-lang.modules" %% "scala-parser-combinators" % "1.0.3",
  "com.typesafe.scala-logging" %% "scala-logging" % "3.1.0",
  "ch.qos.logback" % "logback-classic" % "1.0.9"
)

unmanagedSourceDirectories in Compile += baseDirectory.value / "src" / "main" / "dev"

fork in run := true
