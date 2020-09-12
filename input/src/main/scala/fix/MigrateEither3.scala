/*
rules = [
  "class:fix.MigrateEither",
  "class:fix.MigrateEitherT",
  "class:fix.RemoveGlobalImports"
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

  val res2: DisjunctionT[Option, String, Int] = EitherT.fromDisjunction[Option](res0)

  val res3: Option[String \/ Int] = res2.run

  val res4: String \/ Int = \/.fromEither(Right(20): Either[String, Int])

  val res5: EitherT[Option, String, Int] = EitherT.pure[Option, String, Int](10)

  val res6 = e0.toEither

  def safeToInt(value: String): Throwable \/ Int = \/.fromTryCatchThrowable[Int, Throwable](value.toInt)

  def safeToLong(value: String): Throwable \/ Long = \/.fromTryCatchNonFatal(value.toLong)

}
