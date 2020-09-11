package fix

import cats._
import cats.implicits._

object MigrateOptionSyntax2Test {
  class User(name: String)
  val a: Option[User] = new User("foo").some
  val b: Option[User] = new User("foo").some
}
