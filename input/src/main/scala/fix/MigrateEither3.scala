/*
rules = [
  "class:fix.MigrateEither",
  "class:fix.RemoveGlobalImports",
]
*/
package fix

import scalaz._
import scalaz.Scalaz._

object MigrateEither3Test {
  val e0: String \/ Int = \/-(10)

  val res0: String \/ Int = e0.bimap(error => "msg: " + error, value => value + 10)

  val res1: Option[String \/ Int] =
    e0.bitraverse(error => Option("msg: " + error),value  => Option(value + 10))

}
