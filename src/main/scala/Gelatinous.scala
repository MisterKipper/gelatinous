package gelatinous

import java.nio.file.{Files, Path, Paths}
import java.nio.charset.StandardCharsets

import cats.effect._
// import cats.implicits._
import fs2.io.file.{createDirectories, deleteDirectoryRecursively, directoryStream}

import Util.discard

trait Gelatinous extends IOApp {
  val sourceDir: String
  val targetDir: String
  val directoryHandlers: Map[String, Handler]

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing", "org.wartremover.warts.ToString"))
  def run(args: List[String]): IO[ExitCode] = {
    val source = Paths.get(sourceDir)
    val target = Paths.get(targetDir)

    val main = for {
      blocker <- fs2.Stream.resource(Blocker[IO])
      _ <- fs2.Stream.eval(deleteDirectoryRecursively[IO](blocker, target)) // FIXME: this fails if target doesn't exist
      _ <- fs2.Stream.eval(createDirectories[IO](blocker, target))
      contents <- directoryStream[IO](blocker, source)
      operation = directoryHandlers.getOrElse(contents.getFileName().toString(), SimpleDirectoryHandler)
      _ <- fs2.Stream.eval(operation[IO](blocker, contents, target.resolve(contents.getFileName())))
    } yield ()

    main.compile.drain.as(ExitCode.Success)
  }

  @SuppressWarnings(
    Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing", "org.wartremover.warts.ToString")
  )
  def buildSite[F[_]: Sync: ContextShift](blocker: Blocker, source: Path, target: Path): F[Unit] = {
    ???
    // directoryStream(blocker, source).map { f =>
    //     if (!Files.isDirectory(f)) {
    //       Sync[F].delay(println("Source directory should only contain directories."))
    //     } else {
    //       discard(Sync[F].delay(println(s"We on it: ${f.toString()} to ${target.toString()}.")))
    //       // val handler = directoryHandlers.getOrElse(file.getFileName().toString(), SimpleDirectoryHandler)
    //       // handler.run(blocker, file, target)
    //       Util.copyDirectoryRecursively[F](blocker, f, target)
    //     }
    //   }
  }

  // TODO: Why doesn't this need a Blocker?
  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  def writePage[F[_]: Sync: ContextShift](targetRoot: Path): fs2.Pipe[F, (String, String), Unit] =
    stream =>
      stream.evalMap {
        case (route, html) =>
          Sync[F].delay {
            val targetPath = targetRoot.resolve(route)
            discard(Files.write(targetPath, html.getBytes(StandardCharsets.UTF_8)))
          }
      }
}
