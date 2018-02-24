package ch.netzwerg.demo.server

import java.lang.Math
import java.util.concurrent.atomic.AtomicInteger

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport._
import akka.http.scaladsl.model.StatusCodes._
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server._
import ch.netzwerg.demo.common.Models.Location
import sangria.ast.Document
import sangria.execution._
import sangria.marshalling.sprayJson._
import sangria.parser.QueryParser
import spray.json.{JsObject, JsString, JsValue}

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

object GraphQLServer {

  private val repository = LocationRepo.createDatabase()

  case object TooComplexQuery extends Exception

  private val rejectComplexQueries = QueryReducer.rejectComplexQueries(300, (_: Double, _:LocationRepo) => TooComplexQuery)

  val exceptionHandler: Executor.ExceptionHandler = {
    case (_, TooComplexQuery) => HandledException("Too complex query. Please reduce the field selection")
  }

  def endpoint(requestJSON: JsValue)(implicit e: ExecutionContext): Route = {

    val JsObject(fields) = requestJSON

    val JsString(query) = fields("query")

    val operation = fields.get("operationName") collect {
      case JsString(op) ⇒ op
    }


    val vars = fields.get("variables") match {
      case Some(obj: JsObject) => obj
      case _ => JsObject.empty
    }

    QueryParser.parse(query) match {
      case Success(queryAst) =>
        complete(executeGraphQLQuery(queryAst, operation, vars))
      case Failure(error) =>
        complete(BadRequest, JsObject("error" -> JsString(error.getMessage)))
    }
  }


  val idCounter = new AtomicInteger(5)
  def insertRandomLocation() = {
    val x = Math.round(Math.random()).intValue()
    val y = Math.round(Math.random()).intValue()
    repository.insert(Location(idCounter.getAndIncrement(), x , y))
  }

  private def executeGraphQLQuery(query: Document, op: Option[String], vars: JsObject)(implicit e: ExecutionContext) = {
    Executor.execute(
      SchemaDef.LocationSchema,
      query,
      repository,
      variables = vars,
      operationName = op,
      exceptionHandler = exceptionHandler,
      queryReducers = rejectComplexQueries :: Nil
    ).map(OK -> _)
      .recover {
        case error: QueryAnalysisError => BadRequest -> error.resolveError
        case error: ErrorWithResolver => InternalServerError -> error.resolveError
      }
  }
}
