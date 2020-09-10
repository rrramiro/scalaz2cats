package fix

import fix.ScalafixUtils._
import scalafix.v1._

import scala.meta._
import scala.meta.contrib._

class MigrateOptionSyntax extends SemanticRule("MigrateOptionSyntax") {
  private lazy val scalazOptionSyntaxImport = importer"scalaz.syntax.std.option._"
  private lazy val scalazOptionImport = importer"scalaz.syntax.option._"
  private lazy val catsOptionSyntaxImport = importer"cats.syntax.option._"
  private lazy val some = SymbolMatcher.normalized("scalaz.syntax.std.OptionIdOps.some.")
  private lazy val none = SymbolMatcher.normalized("scalaz.syntax.std.OptionFunctions.none.")

  override def fix(implicit ctx: SemanticDocument): Patch = {
    ctx.tree.collect {
      case t @ importer"scalaz.syntax.std.option._" =>
        Patch.replaceTree(t, catsOptionSyntaxImport.syntax)
      case t @ importer"scalaz.std.option._" =>
        if (!ctx.tree.contains(scalazOptionSyntaxImport))
          Patch.replaceTree(t, catsOptionSyntaxImport.syntax)
        else Patch.removeImportee(t.asInstanceOf[Importer].importees.head)
      case t @ importer"scalaz.std.option._" if !ctx.tree.contains(scalazOptionSyntaxImport) =>
        Patch.replaceTree(t, catsOptionSyntaxImport.syntax)
    }.asPatch + Patch.replaceSymbols(
      "scalaz/syntax/std/OptionOps#`|`()." -> "getOrElse"
    ) + addImportsIfNeeded {
      case Term.Select(_, some(_) | none(_))
          if !ctx.tree.contains(scalazOptionSyntaxImport)
            && !ctx.tree.contains(scalazOptionImport) =>
        catsOptionSyntaxImport
    }
  }
}
