package gelatinous

import cats.effect._
import java.nio.file.{Paths} //, Paths}

trait Gelatinous extends IOApp {
  val assetDir: String
  val sourceDir: String
  val targetDir: String
  val standalonePages: List[Template]
  val collections: List[ArticleCollection[Article]]

  lazy val manifest: Manifest = Manifest(sourceDir, targetDir, assetDir, standalonePages, collections)

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  def run(args: List[String]): IO[ExitCode] = {
    // val source = Paths.get(manifest.sourceDir)
    // val target: Path = Paths.get(manifest.targetDir)
    // val assetSource: Path = Paths.get(manifest.assetDir)

    val pipeline = fs2.Stream.resource(Blocker[IO]).flatMap { blocker =>
      fs2.io.file
        .readAll[IO](Paths.get(manifest.sourceDir).resolve("assets/robots.txt"), blocker, 4096)
        .through(fs2.text.utf8Decode)
        .through(fs2.text.lines)
        .intersperse("\n")
        .through(fs2.text.utf8Encode)
        .through(fs2.io.file.writeAll(Paths.get(manifest.targetDir).resolve("foo.txt"), blocker))
    }
    // fs2.io.file.deleteDirectoryRecursively[F](blocker, target)
    //   // Copy static components
    //   _ <- io.file.copy
    //   // Create standalone pages
    //   // _ <- for {
    //   //   page <- manifest.standalonePages
    //   // } yield page
    //   // Create collections
    // ExitCode.Success
    pipeline.compile.drain.as(ExitCode.Success)
    // Sync[F].delay{
    // }
  }
}
