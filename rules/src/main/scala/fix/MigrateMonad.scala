package fix

import scalafix.v1._
import scala.meta._

class MigrateMonad extends SemanticRule("MigrateMonad") {

  override def fix(implicit ctx: SemanticDocument): Patch =
    ctx.tree.collect {
      case t @ importer"scalaz.Monad" =>
        Patch.addGlobalImport(importer"cats.Monad") + Patch.removeImportee(
          t.asInstanceOf[Importer].importees.head
        )
    }.asPatch + Patch.replaceSymbols(
      "scalaz/Bind#bind()." -> "flatMap"
    )
}
