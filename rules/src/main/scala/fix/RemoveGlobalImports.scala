package fix

import scalafix.v1._
import scala.meta._

class RemoveGlobalImports extends SemanticRule("RemoveGlobalImports") {
  override def fix(implicit ctx: SemanticDocument): Patch = {
    ctx.tree.collect {
      case t @ importer"scalaz._" =>
        Patch.addGlobalImport(importer"cats._") + Patch.removeImportee(
          t.asInstanceOf[Importer].importees.head
        )
      case t @ importer"Scalaz._" =>
        Patch.addGlobalImport(importer"cats.implicits._") + Patch.removeImportee(
          t.asInstanceOf[Importer].importees.head
        )
      case t @ importer"scalaz.Scalaz._" =>
        Patch.addGlobalImport(importer"cats.implicits._") + Patch.removeImportee(
          t.asInstanceOf[Importer].importees.head
        )
    }.asPatch
  }
}
