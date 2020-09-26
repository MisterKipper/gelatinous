package gelatinous

import cats.effect._
import java.nio.file.{Path, Paths}

object Gelatinous {
  def build[F[_]: Sync: ContextShift](blocker: Blocker, manifest: Manifest): F[ExitCode] = {
    // val source = Paths.get(manifest.sourceDir)
    val target: Path = Paths.get(manifest.targetDir)
    // val assetSource: Path = Paths.get(manifest.assetDir)
    blocker.delay {
      fs2.io.file.deleteDirectoryRecursively[F](blocker, target)
      //   // Copy static components
      //   _ <- io.file.copy
      //   // Create standalone pages
      //   // _ <- for {
      //   //   page <- manifest.standalonePages
      //   // } yield page
      //   // Create collections
      ExitCode.Success
    }
  }
}
