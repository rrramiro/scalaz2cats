package fix

import cats._
import cats.data.EitherT
import cats.implicits._
import scala.Right

object MigrateEither3Test {
  val e0: Either[String, Int] = Right(10)

  val res0: Either[String, Int] = e0.bimap(error => "msg: " + error, value => value + 10)

  val res1: Option[Either[String, Int]] =
    e0.bitraverse(error => Option("msg: " + error),value  => Option(value + 10))

  val res2: EitherT[Option, String, Int] = EitherT.fromEither[Option](res0)

  val res3: Option[Either[String, Int]] = res2.value

  val res4: Either[String, Int] = Right(20): Either[String, Int]

  val res5: EitherT[Option, String, Int] = EitherT.pure[Option, String](10)

  val res6 = e0

  def safeToInt(value: String): Either[Throwable, Int] = Either.catchOnly[Throwable](value.toInt)

  def safeToLong(value: String): Either[Throwable, Long] = Either.catchNonFatal(value.toLong)

}
