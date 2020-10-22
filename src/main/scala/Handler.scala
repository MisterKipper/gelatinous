package gelatinous

import java.nio.file.Path
import cats.effect._

abstract class Handler {
  def run[F[_]: Sync: ContextShift](blocker: Blocker, source: Path, target: Path): F[Unit]
}
