/*
rules = [
  "class:fix.MigrateNonEmptyList",
]
*/
package fix

import scalaz.NonEmptyList

object MigrateNonEmptyListTest  {
  val res0: NonEmptyList[Int] = NonEmptyList.nels(10, 20, 30)

}
