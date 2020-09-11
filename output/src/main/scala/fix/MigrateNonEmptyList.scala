package fix

import cats.data.NonEmptyList

object MigrateNonEmptyListTest  {
  val res0: NonEmptyList[Int] = NonEmptyList.of(10, 20, 30)

  val res1 = NonEmptyList("head", List("tail"))

}
