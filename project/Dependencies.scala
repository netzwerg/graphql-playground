import sbt._

object Dependencies {

  val sangria = "org.sangria-graphql" %% "sangria" % "1.2.2"
  val sangriaGraphQL = "org.sangria-graphql" %% "sangria-spray-json" % "1.0.0"
  val akkaHttp = "com.typesafe.akka" %% "akka-http" % "10.0.7"
  val akkaHttpSprayJSON = "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.7"

  val slick = "com.typesafe.slick" %% "slick" % "3.2.0"
  val slf4j = "org.slf4j" % "slf4j-nop" % "1.6.4"
  val slickHikariCP = "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0"
  val h2 = "com.h2database" % "h2" % "1.4.193"

  val scalaTest = "org.scalatest" %% "scalatest" % "3.0.1"

  val serverDependencies = Seq(
    sangria,
    sangriaGraphQL,
    akkaHttp,
    akkaHttpSprayJSON,
    slick,
    slf4j,
    slickHikariCP,
    h2,
    scalaTest % Test
  )

}
