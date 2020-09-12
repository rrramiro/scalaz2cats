package fix

import scalafix.v1._
import scala.meta._

class MigrateEitherT extends SemanticRule("MigrateEitherT") {
  private val pure = SymbolMatcher.exact("scalaz/EitherT.pure().")
  private val disjunctionT = SymbolMatcher.exact("scalaz/package.DisjunctionT#")

  override def fix(implicit ctx: SemanticDocument): Patch =
    ctx.tree.collect {
      case t @ Type.Apply(disjunctionT(_), List(arg1, arg2, arg3)) =>
        Patch.replaceTree(t, s"EitherT[${arg1.syntax}, ${arg2.syntax}, ${arg3.syntax}]") +
          Patch.addGlobalImport(importer"cats.data.EitherT")
      case t @ Term.ApplyType(pure(_), _) =>
        Patch.replaceTree(t, t.copy(targs = t.targs.init).syntax)
    }.asPatch + Patch.replaceSymbols(
      "scalaz/EitherT#run." -> "value",
      "scalaz/EitherT.fromDisjunction()." -> "fromEither"
    )
}
