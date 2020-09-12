package fix

import scalafix.v1._
import scala.meta._

class MigrateNonEmptyList extends SemanticRule("MigrateNonEmptyList") {
  private val nonEmptyListNel = SymbolMatcher.normalized("scalaz/NonEmptyList.nel().")
  private val nonEmptyListList = SymbolMatcher.normalized("scalaz/NonEmptyList.list().")
  private val nonEmptyListApply = SymbolMatcher.normalized("scalaz/NonEmptyList().")
  private val iListFromList = SymbolMatcher.normalized("scalaz/IList.fromList().")
  //TODO import cats.data.NonEmptyList

  override def fix(implicit ctx: SemanticDocument): Patch = {
    ctx.tree.collect {
      case t @ importer"scalaz.NonEmptyList" =>
        Patch.replaceTree(t, importer"cats.data.NonEmptyList".syntax)
      case t @ Term.Apply(
            nonEmptyListNel(_),
            List(arg1, Term.Apply(iListFromList(_), List(arg2)))
          ) =>
        Patch.replaceTree(t, s"NonEmptyList(${arg1.syntax}, ${arg2.syntax})")
      case t @ Term.Apply(nonEmptyListApply(_), args) =>
        Patch.replaceTree(t, s"NonEmptyList.of(${args.map(_.syntax).mkString(", ")})")
      case t @ importer"scalaz.IList" =>
        Patch.removeImportee(t.asInstanceOf[Importer].importees.head)
    }.asPatch + Patch.replaceSymbols(
      "scalaz/NonEmptyList.nels()." -> "of().",
      "scalaz/NonEmptyList.apply()." -> "of().",
      "scalaz/NonEmptyList#list()." -> "toList()."
    )
  }
}
