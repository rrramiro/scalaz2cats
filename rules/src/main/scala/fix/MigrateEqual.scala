package fix

import scalafix.v1._
import scala.meta._

class MigrateEqual extends SemanticRule("MigrateEqual") {

  //private lazy val scalazEqualSyntaxImport = importer"scalaz.syntax.equal._"
  //private lazy val scalazEqualImport = importer"scalaz.Equal"
  private lazy val catsEqualSyntaxImport = importer"cats.syntax.eq._"
  private lazy val catsEqualImport = importer"cats.Eq"

  private[this] val Equal = SymbolMatcher.normalized("scalaz.Equal.")
  private[this] val catsEqual = SymbolMatcher.normalized("cats.Eq.")
  private[this] val `===` = SymbolMatcher.normalized("scalaz.syntax.EqualOps.`===`.")
  private[this] val `=/=` = SymbolMatcher.normalized("scalaz.syntax.EqualOps.`=/=`.")
  private[this] val `/==` = SymbolMatcher.normalized("scalaz.syntax.EqualOps.`/==`.")
  private[this] val `≠` = SymbolMatcher.normalized("scalaz.syntax.EqualOps.`≠`.")
  private[this] val `≟` = SymbolMatcher.normalized("scalaz.syntax.EqualOps.`≟`.")

  override def fix(implicit ctx: SemanticDocument): Patch = {
    ctx.tree.collect {
      case t @ importer"scalaz.syntax.equal._" =>
        Patch.replaceTree(t, catsEqualSyntaxImport.syntax)
      case t @ importer"scalaz.Equal" =>
        Patch.replaceTree(t, catsEqualImport.syntax)
      case t @ Type.Apply(Equal(_), List(tpe)) =>
        Patch.replaceTree(t, q"Eq[$tpe]".syntax)
      case Term.Select(a @ Equal(_), _) =>
        Patch.replaceTree(a, "Eq")
      case Type.Param(_, _, _, _, _, List(a @ Equal(_))) =>
        Patch.replaceTree(a, "Eq")
    }.asPatch + Patch.replaceSymbols(
      "scalaz/syntax/EqualOps#`=/=`()." -> """`=!=`.""",
      "scalaz/syntax/EqualOps#`/==`()." -> "`=!=`.",
      "scalaz/syntax/EqualOps#`≠`()." -> "`=!=`.",
      "scalaz/syntax/EqualOps#`≟`()." -> "`===`.",
      "scalaz/Equal.equalA()." -> "fromUniversalEquals",
      "scalaz.Equal.equalBy" -> "by",
      "scalaz.Equal.equal" -> "eqv"
    )
  }
}
