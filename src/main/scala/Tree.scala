package gelatinous

sealed trait Tree[+A] {
  @SuppressWarnings(Array("org.wartremover.warts.Recursion"))
  def map[B](f: A => B): Tree[B] = this match {
    case Node(v, bs) => Node(f(v), bs.map(_.map(f)))
    case Leaf() => Leaf()
  }
}

final case class Node[A](value: A, branches: List[Tree[A]]) extends Tree[A]

final case class Leaf() extends Tree[Nothing]
