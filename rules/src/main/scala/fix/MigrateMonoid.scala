package fix

import scalafix.v1._

import scala.meta._

class MigrateMonoid extends SemanticRule("MigrateMonad") {

  override def fix(implicit ctx: SemanticDocument): Patch =
    ctx.tree.collect {
      case t @ Defn.Def(_, Term.Name("zero"), _, _, _, _) =>
        Patch.replaceTree(t, t.copy(name = Term.Name("empty")).syntax)
      case t @ Defn.Def(_, Term.Name("append"), _, List(List(f1, f2)), _, _) =>
        Patch.replaceTree(
          t,
          t.copy(
            name = Term.Name("combine"),
            paramss = List(List(f1, f2.copy(decltpe = f1.decltpe)))
          ).syntax
        )
      case t @ importer"scalaz.Monoid" =>
        Patch.addGlobalImport(importer"cats.Monoid") + Patch.removeImportee(
          t.asInstanceOf[Importer].importees.head
        )
    }.asPatch

}
