package fix

import cats._
import cats.implicits._
import scala.Right

object MigrateEither3Test {
  val e0: Either[String, Int] = Right(10)

  val res0: Either[String, Int] = e0.bimap(error => "msg: " + error, value => value + 10)

  val res1: Option[Either[String, Int]] =
    e0.bitraverse(error => Option("msg: " + error),value  => Option(value + 10))

}
