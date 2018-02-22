package ch.netzwerg.demo.server

import sangria.execution.deferred.HasId

object Models {

  type LocationId = Int

  trait Identifiable {
    def id: Int
  }

  case class Location(id: LocationId, x: Int, y: Int) extends Identifiable

  object Location {
    implicit val hasId = HasId[Location, LocationId](_.id)
  }

}
