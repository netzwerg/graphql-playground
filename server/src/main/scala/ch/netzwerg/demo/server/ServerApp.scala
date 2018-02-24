package ch.netzwerg.demo.server

import java.util.concurrent.atomic.AtomicInteger

import akka.actor.{Actor, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import akka.stream.ActorMaterializer
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import spray.json._

import scala.Console._
import scala.concurrent.Await
import scala.language.postfixOps

object ServerApp extends App {

  val PORT = 8080

  implicit val actorSystem = ActorSystem("graphql-server")
  implicit val materializer = ActorMaterializer()

  import actorSystem.dispatcher

  import scala.concurrent.duration._

  logger("Starting GRAPHQL server...")

  val Tick = "tick"

  class TickActor extends Actor {
    def receive = {
      case Tick ⇒
        GraphQLServer.insertRandomLocation()
    }
  }

  val tickActor = actorSystem.actorOf(Props(classOf[ServerApp.TickActor]))

  //This will schedule to send the Tick-message
  //to the tickActor after 0ms repeating every 50ms
  val cancellable = actorSystem.scheduler.schedule(
    0 milliseconds,
    1 seconds,
    tickActor,
    Tick)

  //shutdown Hook
  scala.sys.addShutdownHook(() -> shutdown())

  val route: Route = cors() {
    (post & path("graphql")) {
      entity(as[JsValue]) { requestJson =>
        GraphQLServer.endpoint(requestJson)
      }
    } ~ {
      getFromResource("graphiql.html")
    }
  }

  Http().bindAndHandle(route, "0.0.0.0", PORT)

  logger(s"open a browser with URL: http://localhost:$PORT")
  logger(s"or POST queries to http://localhost:$PORT/graphql")

  def shutdown(): Unit = {

    logger("Terminating...", YELLOW)
    cancellable.cancel()
    actorSystem.terminate()
    Await.result(actorSystem.whenTerminated, 30 seconds)
    logger("Terminated... Bye", YELLOW)
  }

  private def logger(message: String, color: String = GREEN): Unit = {
    println(color + message)
  }

}
