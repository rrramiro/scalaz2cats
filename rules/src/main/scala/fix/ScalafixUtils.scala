package fix

import scalafix.v1._
import scala.meta._
import scala.meta.contrib._

object ScalafixUtils {
  def addImportsIfNeeded(
      checker: PartialFunction[Tree, Importer]
  )(implicit ctx: SemanticDocument): Patch = {
    ctx.tree
      .collectFirst(checker)
      .map(importer => Patch.addGlobalImport(importer))
      .asPatch
  }

  def replaceOpWithComma(op: Term.Name)(implicit ctx: SemanticDocument): Patch =
    Patch.replaceTree(op, ",") + removeWhiteSpacesIfNeeded(op)

  def removeWhiteSpacesIfNeeded(op: Term)(implicit ctx: SemanticDocument): Patch = {
    ctx.tokenList
      .leading(op.tokens.head)
      .takeWhile(_.is[Whitespace])
      .map(Patch.removeToken)
      .asPatch
  }

  def wrapInParensIfNeeded(t: Term)(implicit ctx: SemanticDocument): Patch = {
    for {
      head <- t.tokens.headOption
      if !head.is[Token.LeftParen]
      last <- t.tokens.lastOption
      if !last.is[Token.RightParen]
    } yield Patch.addLeft(head, "(") +
      Patch.addRight(last, ")")
  }.asPatch
}
