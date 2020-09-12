/*
rules = [
  "class:fix.ScalazToCats"
]
*/
package fix

import scalaz._

class MigrateValidation2 {
  val res0: Validation[Throwable, Int] = Validation.fromTryCatchNonFatal("10".toInt)
}
