package gelatinous

// import java.io.IOException
import java.nio.file._
import java.nio.file.attribute.BasicFileAttributes
import java.text.SimpleDateFormat
import scala.jdk.CollectionConverters._

import cats.effect._
// import fs2.io.file

object Util {
  val dateFormatString = "yyyy-MM-dd"
  def convertStringToDate(s: String): java.util.Date = {
    val dateFormat = new SimpleDateFormat(dateFormatString)
    dateFormat.parse(s)
  }

  def Descending[T: Ordering]: scala.math.Ordering[T] = implicitly[Ordering[T]].reverse

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  def operateOnDirectory[F[_]: Sync: ContextShift](
      blocker: Blocker,
      path: Path,
      fileVisitor: FileVisitor[Path]
      // options: Set[FileVisitOption] = Set.empty
  ): F[Unit] = {
    blocker.delay(discard(Files.walkFileTree(path, Set.empty.asJava, Int.MaxValue, fileVisitor)))
  }

  def listDirectory(dir: Path): IO[List[Path]] = {
    IO.delay(Files.list(dir).iterator().asScala.toList)
  }

  @SuppressWarnings(Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing"))
  def copyDirectoryRecursively[F[_]: Sync: ContextShift](
      blocker: Blocker,
      source: Path,
      target: Path
  ): F[Unit] =
    operateOnDirectory(
      blocker,
      source,
      new SimpleFileVisitor[Path] {
        override def preVisitDirectory(path: Path, attrs: BasicFileAttributes): FileVisitResult = {
          discard(Files.createDirectory(target.resolve(source.relativize(path))))
          FileVisitResult.CONTINUE
        }
        override def visitFile(path: Path, attrs: BasicFileAttributes): FileVisitResult = {
          discard(Files.copy(path, target.resolve(source.relativize(path))))
          FileVisitResult.CONTINUE
        }
      }
    )

  // def writeFile(targetPath: Path, data: String, route: String): IO[Unit] = {
  //   for {
  //     filePath <- IO(targetPath.resolve(Paths.get(route)))
  //     _ <- IO(Files.createDirectories(filePath.getParent))
  //     _ <- IO(Files.createFile(filePath))
  //     _ <- IO(Files.write(filePath, data.getBytes))
  //   } yield ()
  // }

  @specialized
  def discard[A](evaluateForSideEffectOnly: A): Unit = {
    val _ = evaluateForSideEffectOnly
    ()
  }
}
