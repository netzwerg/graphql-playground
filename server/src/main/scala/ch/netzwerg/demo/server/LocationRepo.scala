package ch.netzwerg.demo.server

import ch.netzwerg.demo.server.Models._
import Models._
import slick.jdbc.H2Profile.api._

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._
import scala.concurrent.{Await, Future}
import scala.language.postfixOps

class LocationRepo(db: Database) {

  import LocationRepo._

  def allLocations = db.run(Locations.result)

  def locations(ids: Seq[LocationId]): Future[Seq[Location]] = db.run(Locations.filter(_.id inSet ids).result)

}

object LocationRepo {

  val Locations = TableQuery[LocationTable]

  class LocationTable(tag: Tag) extends Table[Location](tag, "LOCATIONS") {
    def id = column[LocationId]("LOCATION_ID", O.PrimaryKey)

    def x = column[Int]("X")

    def y = column[Int]("Y")

    def * = (id, x, y) <> ((Location.apply _).tupled, Location.unapply)
  }

  val databaseSetup = DBIO.seq(
    Locations.schema.create,
    Locations ++= Seq(
      Location(1, 0, 0),
      Location(2, 1, 0),
      Location(3, 1, 1),
      Location(4, 1, 0)
    )
  )

  def createDatabase() = {
    val db = Database.forConfig("h2mem")

    Await.result(db.run(databaseSetup), 10 seconds)

    new LocationRepo(db)
  }

}
