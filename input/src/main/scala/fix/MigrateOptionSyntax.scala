/*
rule = "class:fix.MigrateOptionSyntax"
*/
package fix

import scalaz.syntax.std.option._
import scalaz.std.option._

object MigrateOptionSyntaxTest {
  class User(name: String)
  val a: Option[User] = new User("foo").some
  val ab: Int = 10.some | 10
  none[Int]
}
