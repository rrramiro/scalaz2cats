/*
rules = [
  "class:fix.MigrateNonEmptyList"
]
*/
package fix

import scalaz.NonEmptyList
import scalaz.IList

object MigrateNonEmptyListTest  {
  val res0: NonEmptyList[Int] = NonEmptyList.nels(10, 20, 30)

  val res1 = NonEmptyList.nel("head", IList.fromList(List("tail")))

  val res2 = NonEmptyList(10, 20, 30).list

  val res3: NonEmptyList[Int] = NonEmptyList(10, Seq(20, 30): _ *)

  val res4: NonEmptyList[Int] = NonEmptyList.apply(10)

}
