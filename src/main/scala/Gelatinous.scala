package gelatinous

import java.nio.file.{Paths}
import java.nio.charset.StandardCharsets
import cats.effect._
import cats.implicits._
import fs2.io.file
import java.nio.file.Files
import java.nio.file.Path
import scala.jdk.CollectionConverters._

trait Gelatinous extends IOApp {
  val sourceDir: String
  val targetDir: String
  val directoryHandlers: Map[String, Handler]

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  def run(args: List[String]): IO[ExitCode] = {
    val source = Paths.get(sourceDir)
    val target = Paths.get(targetDir)

    val main = for {
      blocker <- fs2.Stream.resource(Blocker[IO])
      target <- fs2.Stream.eval(file.createDirectories[IO](blocker, target))
      _ <- fs2.Stream.eval(
        file.deleteDirectoryRecursively[IO](blocker, target) >>
          file.createDirectories[IO](blocker, target) >>
          buildSite[IO](blocker, source, target)
      )
    } yield ()

    main.compile.drain.as(ExitCode.Success)
  }

  @SuppressWarnings(
    Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing", "org.wartremover.warts.ToString")
  )
  def buildSite[F[_]: Sync: ContextShift](blocker: Blocker, source: Path, target: Path): F[Unit] = {
    blocker.delay {
      Util.discard(
        for (file <- Files.list(source).iterator.asScala) {
          if (!Files.isDirectory(file)) {
            println("Source directory should only contain directories.")
          } else {
            val handler = directoryHandlers.getOrElse(file.getFileName().toString(), SimpleDirectoryHandler)
            handler.run(blocker, file, target)
          }
        }
      )
    }
  }

  // TODO: Why doesn't this need a Blocker?
  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  def writePage[F[_]: Sync: ContextShift](targetRoot: Path): fs2.Pipe[F, (String, String), Unit] =
    stream =>
      stream.evalMap {
        case (route, html) =>
          Sync[F].delay {
            val targetPath = targetRoot.resolve(route)
            Util.discard(Files.write(targetPath, html.getBytes(StandardCharsets.UTF_8)))
          }
      }
}
