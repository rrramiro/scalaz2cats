package fix

import cats.syntax.either._

object Mylibrary_1_0_Test2 {
  def myMethod(x: Int): Either[String, Int] =
    if (x > 2) x.asRight
    else "nope".asLeft
}
