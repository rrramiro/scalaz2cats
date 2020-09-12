package fix

import cats._
import cats.data.{ Validated, ValidatedNel }

class MigrateValidationNel2 {
  def fun(x: Int): ValidatedNel[String, Int] = Validated.condNel(((_ > 2): Int => Boolean)(x), x, "msg")
}
