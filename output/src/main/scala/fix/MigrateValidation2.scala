package fix

import cats._
import cats.data.Validated

class MigrateValidation2 {
  val res0: Validated[Throwable, Int] = Validated.catchNonFatal("10".toInt)
}
