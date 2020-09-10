package fix
import fix.ScalafixUtils._
import scalafix.v1._
import scala.meta._
import scala.meta.contrib._

class MigrateBoolean extends SemanticRule("MigrateBoolean") {
  private lazy val scalazBooleanSyntaxImport = importer"scalaz.syntax.std.boolean._"
  private lazy val catsBooleanSyntaxImport = importer"mouse.boolean._"
  private val booleanSymbolOr = SymbolMatcher.normalized(
    "scalaz.syntax.std.BooleanOps.`?`."
  )

  private val booleanSymbolOrTwo =
    SymbolMatcher.normalized("scalaz.syntax.std.BooleanOps#Conditional#`|`.")
  private val booleanEither = SymbolMatcher.normalized("scalaz.syntax.std.BooleanOps.either.")
  private val booleanEitherOr =
    SymbolMatcher.normalized("scalaz.syntax.std.BooleanOps.ConditionalEither.or.")

  // TODO: also need to support a `true.either(12).or("abc")` case
  override def fix(implicit doc: SemanticDocument): Patch = {
    doc.tree.collect {
      case t @ importer"scalaz.syntax.std.boolean._" =>
        Patch.replaceTree(t, catsBooleanSyntaxImport.syntax)
      case Term.ApplyInfix(
            Term.ApplyInfix(_, op1 @ booleanSymbolOr(_), _, List(firstArg)),
            op2 @ booleanSymbolOrTwo(_),
            _,
            List(secondArg)
          ) =>
        Patch.replaceTree(op1, "fold") +
          replaceOpWithComma(op2) +
          removeWhiteSpacesIfNeeded(firstArg) +
          Patch.addLeft(firstArg, "(") +
          Patch.addRight(secondArg, ")")
      case t @ Term.ApplyInfix(
            Term.ApplyInfix(_, booleanSymbolOr(op1: Term.Name), _, List(firstArg)),
            booleanSymbolOrTwo(op2: Term.Name),
            _,
            List(secondArg)
          ) =>
        Patch.replaceTree(op1, "fold") +
          replaceOpWithComma(op2) +
          removeWhiteSpacesIfNeeded(firstArg) +
          Patch.addLeft(firstArg, "(") +
          Patch.addRight(secondArg, ")")

      case t @ Term.ApplyInfix(
            Term.ApplyInfix(_, booleanEither(_), _, List(firstArg)),
            booleanEitherOr(op2: Term.Name),
            _,
            List(secondArg)
          ) =>
        replaceOpWithComma(op2) +
          removeWhiteSpacesIfNeeded(firstArg) + Patch.addLeft(firstArg, "(") +
          Patch.replaceTree(firstArg, secondArg.syntax) + Patch.replaceTree(
          secondArg,
          firstArg.syntax
        ) + Patch
          .addRight(secondArg, ")")
    }.asPatch + addImportsIfNeeded {
      case Term.ApplyInfix(_, booleanSymbolOr(_) | booleanEither(_), _, _)
          if !doc.tree.contains(scalazBooleanSyntaxImport) =>
        catsBooleanSyntaxImport
    }
  }

}
