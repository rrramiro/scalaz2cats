package fix

import scalafix.v1._

class ScalazToCats extends SemanticRule("MigrateBoolean") {
  val migrateBoolean = new MigrateBoolean
  val migrateEither = new MigrateEither
  val migrateEqual = new MigrateEqual
  val migrateNonEmptyList = new MigrateNonEmptyList
  val migrateOptionSyntax = new MigrateOptionSyntax
  val migrateValidation = new MigrateValidation
  val migrateValidationNel = new MigrateValidationNel
  val removeGlobalImports = new RemoveGlobalImports
  val migrateMonad = new MigrateMonad
  val migrateMonoid = new MigrateMonoid

  override def fix(implicit doc: SemanticDocument): Patch = {
    Patch.fromIterable(
      List(
        migrateBoolean.fix(doc),
        migrateEither.fix(doc),
        migrateEqual.fix(doc),
        migrateNonEmptyList.fix(doc),
        migrateOptionSyntax.fix(doc),
        migrateValidation.fix(doc),
        migrateValidationNel.fix(doc),
        removeGlobalImports.fix(doc),
        migrateMonad.fix(doc),
        migrateMonoid.fix(doc)
      )
    )
  }
}
