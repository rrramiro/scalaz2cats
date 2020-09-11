package fix

import scalafix.v1._
import scala.meta._

class MigrateNonEmptyList extends SemanticRule("MigrateNonEmptyList") {

  override def fix(implicit ctx: SemanticDocument): Patch = {
    ctx.tree.collect {
      case t @ importer"scalaz.NonEmptyList" =>
        Patch.replaceTree(t, importer"cats.data.NonEmptyList".syntax)
    }.asPatch + Patch.replaceSymbols(
      "scalaz/NonEmptyList.nels()." -> "of()."
    )
  }
}
