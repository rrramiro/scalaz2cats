/*
rules = [
  "class:fix.ScalazToCats"
]
*/
package fix

import scalaz._

final case class Comment(value: String) extends AnyVal

class MigrateMonoidTest {

  implicit val commentMonoid = new Monoid[Comment] {
    override def zero: Comment = Comment("")

    override def append(f1: Comment, f2: => Comment): Comment = Comment(s"${f1.value}\n${f2.value}")
  }

}
