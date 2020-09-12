package fix

import cats._

final case class Comment(value: String) extends AnyVal

class MigrateMonoidTest {

  implicit val commentMonoid = new Monoid[Comment] {
    override def empty: Comment = Comment("")

    override def combine(f1: Comment, f2: Comment): Comment = Comment(s"${f1.value}\n${f2.value}")
  }

}