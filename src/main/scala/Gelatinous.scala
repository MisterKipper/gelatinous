package gelatinous

import java.nio.file.{Paths} //, Paths}
import cats.effect._
import cats.implicits._
import fs2.io.file
import java.nio.file.Files
import java.nio.file.SimpleFileVisitor
import java.nio.file.attribute.BasicFileAttributes
import java.nio.file.FileVisitResult
import java.nio.file.Path
// import java.nio.file.Files

trait Gelatinous extends IOApp {
  val assetDir: String
  val sourceDir: String
  val targetDir: String
  val standalonePages: List[Template]
  val collections: List[ArticleCollection[Article]]

  lazy val manifest: Manifest = Manifest(sourceDir, targetDir, assetDir, standalonePages, collections)

  @SuppressWarnings(
    Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing", "org.wartremover.warts.ToString")
  )
  def run(args: List[String]): IO[ExitCode] = {
    val target = Paths.get(manifest.targetDir)
    val source = Paths.get(manifest.sourceDir)
    val assetSource = source.resolve(Paths.get(manifest.assetDir))

    val content = build(manifest)
    val main = for {
      blocker <- fs2.Stream.resource(Blocker[IO])
      target <- fs2.Stream.eval(file.createDirectories[IO](blocker, target))
      _ <- fs2.Stream.eval(
        file.deleteDirectoryRecursively[IO](blocker, target) >>
          file.createDirectories[IO](blocker, target) >>
          copyDirectoryRecursively[IO](blocker, assetSource, target)
      )
      _ <- fs2.Stream.eval(content.write[IO](blocker, target))
      // _ <- file.copy(blocker, fileSource, fileTarget)
    } yield ()
    // file.deleteDirectoryRecursively[F](blocker, target)
    //   // Copy static components
    //   _ <- io.file.copy
    //   // Create standalone pages
    //   // _ <- for {
    //   //   page <- manifest.standalonePages
    //   // } yield page
    //   // Create collections
    // ExitCode.Success
    main.compile.drain.as(ExitCode.Success)
    // Sync[F].delay{
    // }
  }

  class SiteContent(content: Map[Path, Template]) {
    @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
    def write[F[_]: Sync: ContextShift](blocker: Blocker, targetRoot: Path): F[Unit] = {
      import java.nio.charset.StandardCharsets
      blocker.delay {
        content.foreach { case (path: Path, template: Template) =>
          val targetPath = targetRoot.resolve(path)
          Util.discard(Files.write(targetPath, template.render.getBytes(StandardCharsets.UTF_8)))
        }
      }
    }
  }

  def build(manifest: Manifest): SiteContent = {
    ???
  }

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  def copyDirectoryRecursively[F[_]: Sync: ContextShift](blocker: Blocker, source: Path, target: Path): F[Unit] = {
    blocker.delay {
      Util.discard(
        Files.walkFileTree(
          source,
          new SimpleFileVisitor[Path] {
            override def visitFile(path: Path, attrs: BasicFileAttributes): FileVisitResult = {
              val targetPath = target.resolve(source.relativize(path))
              Util.discard(Files.copy(path, targetPath))
              FileVisitResult.CONTINUE
            }
            override def preVisitDirectory(path: Path, attrs: BasicFileAttributes): FileVisitResult = {
              val targetPath = target.resolve(source.relativize(path))
              if (!Files.exists(targetPath)) {
                Util.discard(Files.createDirectory(targetPath))
              }
              FileVisitResult.CONTINUE
            }
          }
        )
      )
      ()
    }
  }
}
