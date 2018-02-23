package ch.netzwerg.demo.common

object Models {

  type LocationId = Int

  trait Identifiable {
    def id: Int
  }

  case class Location(id: LocationId, x: Int, y: Int) extends Identifiable

}
