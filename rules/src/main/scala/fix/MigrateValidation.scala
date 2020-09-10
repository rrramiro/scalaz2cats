package fix

import scalafix.v1._

class MigrateValidation extends SemanticRule("MigrateValidation") {
  override def fix(implicit ctx: SemanticDocument): Patch = {
    Patch.replaceSymbols(
      "scalaz.Validation" -> "cats.data.Validated",
      "scalaz.Success" -> "cats.data.Validated.Valid",
      "scalaz.Failure" -> "cats.data.Validated.Invalid"
    )
  }
}
