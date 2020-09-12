/*
rules = [
  "class:fix.MigrateValidationNel",
  "class:fix.RemoveGlobalImports"
]
*/
package fix

import scalaz._

class MigrateValidationNel2 {
  def fun(x: Int): ValidationNel[String, Int] = Validation.liftNel(x)( _ > 2, "msg")
}
