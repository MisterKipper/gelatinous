package gelatinous

import java.nio.file.Path
import cats.effect._

abstract class Handler {
  def apply[F[_]: Sync: ContextShift](blocker: Blocker, source: Path, target: Path): F[Unit]
  def buildPathTree[F[_]: Sync: ContextShift](blocker: Blocker, source: Path): F[Tree[ContentItem]] = {
    ???
  }
}
