package gelatinous

import java.nio.file.Path
import cats.effect._

@SuppressWarnings(Array("org.wartremover.warts.ToString"))
class SimpleDirectoryHandler extends Handler {
  def apply[F[_]: Sync: ContextShift](blocker: Blocker, source: Path, target: Path): F[Unit] = {
    Util.copyDirectoryRecursively[F](blocker, source, target)
  }
}

object SimpleDirectoryHandler extends SimpleDirectoryHandler
