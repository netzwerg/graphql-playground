import Dependencies._

lazy val sharedSettings = Seq(
  organization := "ch.netzwerg",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.12.4"
)

lazy val global = project
  .in(file("."))
  .aggregate(
    server,
    common,
    client
  )
  .disablePlugins(RevolverPlugin)

lazy val server = project
  .settings(
    sharedSettings,
    libraryDependencies ++= serverDependencies
  )
  .dependsOn(common)

lazy val common = project
  .settings(sharedSettings)
  .disablePlugins(RevolverPlugin)

lazy val client = project
  .settings(sharedSettings)
  .dependsOn(common)
  .disablePlugins(RevolverPlugin)