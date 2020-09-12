package fix

import cats.Monad

class MigrationMonadTest {
  def flatMap[F[_], A, B](fa: F[A])(ab: A => F[B])(implicit F: Monad[F]): F[B] = F.flatMap[A, B](fa)(ab)

}