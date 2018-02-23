import org.scalajs.sbtplugin.ScalaJSPlugin.autoImport._
import sbt._

object Dependencies {

  val serverDependencies = Def.setting(
    Seq(
      "org.sangria-graphql" %% "sangria" % "1.2.2",
      "org.sangria-graphql" %% "sangria-spray-json" % "1.0.0",
      "com.typesafe.akka" %% "akka-http" % "10.0.7",
      "com.typesafe.akka" %% "akka-http-spray-json" % "10.0.7",
      "com.typesafe.slick" %% "slick" % "3.2.0",
      "org.slf4j" % "slf4j-nop" % "1.6.4",
      "com.typesafe.slick" %% "slick-hikaricp" % "3.2.0",
      "com.h2database" % "h2" % "1.4.193",
      "org.scalatest" %% "scalatest" % "3.0.1" % Test
    )
  )

  val clientDependencies = Def.setting(
    Seq(
      "org.scala-js" %%% "scalajs-dom" % "0.9.4",
      "com.github.japgolly.scalajs-react" %%% "core" % "1.1.1"
    )
  )

  val reactVersion = "15.6.1"

  val clientJsDependencies = Def.setting(
    Seq(
      "org.webjars.bower" % "react" % reactVersion / "react-with-addons.js" minified "react-with-addons.min.js" commonJSName "React",
      "org.webjars.bower" % "react" % reactVersion / "react-dom.js" minified "react-dom.min.js" dependsOn "react-with-addons.js" commonJSName "ReactDOM"
    )
  )

}
