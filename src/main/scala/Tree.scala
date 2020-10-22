package gelatinous

// import cats.Applicative
// import cats.Traverse

sealed trait Tree[+A] {
  @SuppressWarnings(Array("org.wartremover.warts.Recursion"))
  def traverse[B](f: A => B): Tree[B] = this match {
    case Node(v, bs) => Node(f(v), bs.map(_.traverse(f)))
    case Leaf() => Leaf()
  }
}
final case class Node[A](value: A, branches: List[Tree[A]]) extends Tree[A]
final case class Leaf() extends Tree[Nothing]
