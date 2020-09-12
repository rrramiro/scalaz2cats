package fix

import cats.data.NonEmptyList

object MigrateNonEmptyListTest  {
  val res0: NonEmptyList[Int] = NonEmptyList.of(10, 20, 30)

  val res1 = NonEmptyList("head", List("tail"))

  val res2 = NonEmptyList.of(10, 20, 30).toList

  val res3: NonEmptyList[Int] = NonEmptyList.of(10, Seq(20, 30): _ *)

  val res4: NonEmptyList[Int] = NonEmptyList.of(10)

}
