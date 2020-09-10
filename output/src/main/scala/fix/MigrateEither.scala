package fix

import cats.syntax.either._
import scala.{ Left, Right }

object Mylibrary_1_0_Test {
  def myMethod(x: Int): Either[String, Int] =
    if (x > 2) Right(x)
    else "nope".asLeft

  def myMethod2(x: Int): Either[String, Int] = ???

  myMethod(43) match {
    case Right(n) => println(n)
    case Left(s) => println(s)
  }

  myMethod2(1) match {
    case scala.Right(n) => println(n)
    case scala.Left(s) => println(s)
  }

  myMethod2(1) getOrElse 0
}
