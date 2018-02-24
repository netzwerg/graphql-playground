package ch.netzwerg.demo.client

import japgolly.scalajs.react._
import japgolly.scalajs.react.vdom.html_<^._
import org.scalajs.dom._

object ClientApp {

  val HelloMessage = ScalaComponent.builder[String]("HelloMessage")
    .render($ => <.div("Hello ", $.props))
    .build

  def main(args: Array[String]): Unit = {
    val container = document.getElementById("root")
    HelloMessage("Scala.js/React").renderIntoDOM(container)
  }

}