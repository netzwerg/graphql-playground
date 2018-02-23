package ch.netzwerg.demo.server

import ch.netzwerg.demo.common.Models.{Location, LocationId}
import sangria.execution.deferred.HasId

object Models {

  object Location {
    implicit val hasId = HasId[Location, LocationId](_.id)
  }

}
