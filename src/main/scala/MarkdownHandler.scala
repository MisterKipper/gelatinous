package gelatinous

import java.nio.file.Path
import cats.effect._

class MarkdownHandler extends Handler {
  def run[F[_]: Sync: ContextShift](blocker: Blocker, source: Path, target: Path): F[Unit] = {
    ???
  }
}

object MarkdownHandler extends MarkdownHandler
