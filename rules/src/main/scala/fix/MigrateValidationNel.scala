package fix

import fix.ScalafixUtils._
import scalafix.v1._
import scala.meta._
import scala.meta.contrib._

class MigrateValidationNel extends SemanticRule("MigrateValidationNel") {
  private lazy val scalazValidationSyntaxImport = importer"scalaz.syntax.validation._"
  private lazy val catsValidatedSyntaxImport = importer"cats.syntax.validated._"

  private lazy val nonEmptyListScalaz = SymbolMatcher.normalized("scalaz.NonEmptyList.")
  private lazy val successNel = SymbolMatcher.normalized("scalaz.syntax.ValidationOps.successNel.")
  private lazy val failureNel = SymbolMatcher.normalized("scalaz.syntax.ValidationOps.failureNel.")

  private[this] val cartesianBuilders = SymbolMatcher.normalized(
    ("scalaz.syntax.ApplyOps.`|@|`." ::
      "scalaz.syntax.ApplicativeBuilder.`|@|`." ::
      (3 to 12).map { i: Int =>
        val post = (3 to i)
          .map { j =>
            s"ApplicativeBuilder$j"
          }
          .mkString(".")
        s"scalaz.syntax.ApplicativeBuilder.$post.`|@|`."
      }.toList): _*
  )

  private[this] val cartesianAppliesRenames: Map[String, String] = {
    val applicativeArityBuilders = (3 to 12).map { i: Int =>
      val post = (3 to i)
        .map { j =>
          s"ApplicativeBuilder$j"
        }
        .mkString(".")
      s"scalaz.syntax.ApplicativeBuilder.$post.apply." -> "mapN"
    }.toList

    (Seq(
      s"scalaz.syntax.ApplicativeBuilder.apply." -> "mapN"
    ) ++ applicativeArityBuilders).toMap
  }

  private[this] val cartesianOps =
    SymbolMatcher.normalized(cartesianAppliesRenames.keys.toSeq: _*)

  private def addValidatedSyntaxImportIfNeeded(implicit ctx: SemanticDocument): Patch =
    addImportsIfNeeded {
      case Term.Select(_, successNel(_) | failureNel(_))
          if !ctx.tree.contains(scalazValidationSyntaxImport) =>
        catsValidatedSyntaxImport
    }

  override def fix(implicit ctx: SemanticDocument): Patch = {
    Patch.replaceSymbols(
      "scalaz.ValidationNel" -> "cats.data.ValidatedNel",
      "scalaz.Success" -> "cats.data.Validated.Valid",
      "scalaz.Failure" -> "cats.data.Validated.Invalid",
      "scalaz.syntax.ValidationOps.successNel" -> "validNel",
      "scalaz.syntax.ValidationOps.failureNel" -> "invalidNel"
    ) + ctx.tree.collect {
      case t @ importer"scalaz.syntax.validation._" =>
        Patch.replaceTree(t, importer"cats.syntax.validated._".syntax)
      case t @ importer"scalaz.syntax.apply._" =>
        Patch.replaceTree(t, importer"cats.syntax.apply._".syntax)
      case importer"scalaz.{..$ips}" =>
        ips.collect {
          case nonEmptyListScalaz(i: Importee) =>
            Patch.removeImportee(i) + Patch.addGlobalImport(importer"cats.data.NonEmptyList")
        }.asPatch
      case Term.ApplyInfix(_, cartesianBuilders(op: Term.Name), _, _) =>
        replaceOpWithComma(op)
      case Term.ApplyInfix(lhs, cartesianOps(_), _, _) =>
        wrapInParensIfNeeded(lhs)
      case Term.Apply(t @ Term.ApplyInfix(_, cartesianBuilders(_), _, _), _) =>
        Patch.addRight(t, ".mapN")
      case Term.Apply(nonEmptyListScalaz(t), _) =>
        Patch.addRight(t, ".of")
    }.asPatch +
      Patch.replaceSymbols(cartesianAppliesRenames.toSeq: _*) +
      addValidatedSyntaxImportIfNeeded(ctx)
  }

}
