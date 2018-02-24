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
    `scalajs-client`
  )
  .disablePlugins(RevolverPlugin)

lazy val server = project
  .settings(
    sharedSettings,
    libraryDependencies ++= serverDependencies.value
  )
  .dependsOn(common)

lazy val common = project
  .settings(sharedSettings)
  .disablePlugins(RevolverPlugin)

lazy val `scalajs-client` = project
  .settings(
    sharedSettings,
    libraryDependencies ++= clientDependencies.value,
    jsDependencies ++= clientJsDependencies.value,
    skip in packageJSDependencies := false, // yes, we want to package JS dependencies
    scalaJSUseMainModuleInitializer := true
  )
  .dependsOn(common)
  .enablePlugins(ScalaJSPlugin)
  .disablePlugins(RevolverPlugin)