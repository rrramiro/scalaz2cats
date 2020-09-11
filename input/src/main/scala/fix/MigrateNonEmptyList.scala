/*
rules = [
  "class:fix.MigrateNonEmptyList",
]
*/
package fix

import scalaz.NonEmptyList
import scalaz.IList

object MigrateNonEmptyListTest  {
  val res0: NonEmptyList[Int] = NonEmptyList.nels(10, 20, 30)

  val res1 = NonEmptyList.nel("head", IList.fromList(List("tail")))

}
