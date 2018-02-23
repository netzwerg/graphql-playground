package ch.netzwerg.demo.server

import sangria.schema._

object SchemaDef {

  import ch.netzwerg.demo.common.Models._
  import sangria.macros.derive._

  val IdentifiableType = InterfaceType(
    "Identifiable",
    "Entity that can be identified",
    fields[Unit, Identifiable](
      Field("id", IntType, resolve = _.value.id)
    )
  )

  implicit val LocationType: ObjectType[Unit, Location] =
    deriveObjectType[Unit, Location](
      ObjectTypeDescription("The location")
    )

  val QueryType = ObjectType(
    "Query",
    fields[LocationRepo, Unit](
      Field("allLocations", ListType(LocationType),
        description = Some("Returns a list of all available products."),
        complexity = constantComplexity(100),
        resolve = _.ctx.allLocations
      )
    )
  )

  val LocationSchema = Schema(QueryType)

  def constantComplexity[Ctx](complexity: Double) =
    Some((_: Ctx, _: Args, child: Double) ⇒ child + complexity)

}
