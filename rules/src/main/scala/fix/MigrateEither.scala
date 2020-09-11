package fix

import scalafix.v1._
import scala.meta._

class MigrateEither extends SemanticRule("MigrateEither") {
  //private lazy val scalazEitherSyntaxImport = importer"scalaz.syntax.either._"
  private lazy val catsEitherSyntaxImport = importer"cats.syntax.either._"

  private lazy val \/ = SymbolMatcher.normalized("scalaz.`\\/`.")
  private lazy val \/- = SymbolMatcher.normalized("scalaz.`\\/-`.")
  private lazy val -\/ = SymbolMatcher.normalized("scalaz.`-\\/`.")
  private lazy val left = SymbolMatcher.normalized("scalaz.syntax.EitherOps.left.")
  private lazy val right = SymbolMatcher.normalized("scalaz.syntax.EitherOps.right.")

  override def fix(implicit ctx: SemanticDocument): Patch = {
    ctx.tree.collect {
      case t @ Type.ApplyInfix(lhs, \/(_), rhs) =>
        Patch.replaceTree(t, q"Either[$lhs, $rhs]".syntax)
      case t @ Type.Apply(\/(_), List(rtpe, ltpe)) =>
        Patch.replaceTree(t, q"Either[$rtpe, $ltpe]".syntax)
      case t @ importer"scalaz.syntax.either._" =>
        Patch.replaceTree(t, catsEitherSyntaxImport.syntax)
      case t @ Importee.Name(\/(_) | \/-(_) | -\/(_)) =>
        Patch.removeImportee(t)
    }.asPatch + Patch.replaceSymbols(
      "scalaz.syntax.EitherOps.left" -> "asLeft",
      "scalaz.syntax.EitherOps.right" -> "asRight",
      """scalaz/`\/`#`|`().""" -> "getOrElse",
      """scalaz/`\/-`.""" -> "scala.Right",
      """scalaz/`-\/`.""" -> "scala.Left"
    )
  }
}
