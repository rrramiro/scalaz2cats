/*
rules = [
  "class:fix.ScalazToCats"
]
*/
package fix

import scalaz.Monad

class MigrationMonadTest {
  def flatMap[F[_], A, B](fa: F[A])(ab: A => F[B])(implicit F: Monad[F]): F[B] = F.bind[A, B](fa)(ab)

}
