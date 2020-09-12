package fix

import scalafix.v1._
import scala.meta._

class MigrateValidation extends SemanticRule("MigrateValidation") {
  private val validation = SymbolMatcher.exact("scalaz/package.Validation#")

  override def fix(implicit ctx: SemanticDocument): Patch = {
    ctx.tree.collect {
      case t @ Type.Apply(validation(_), _) =>
        Patch.addGlobalImport(importer"cats.data.Validated")
    }.asPatch + Patch.replaceSymbols(
      "scalaz.Validation" -> "cats.data.Validated",
      "scalaz.Success" -> "cats.data.Validated.Valid",
      "scalaz.Failure" -> "cats.data.Validated.Invalid",
      "scalaz/Validation.fromTryCatchNonFatal()." -> "catchNonFatal"
    )
  }
}
