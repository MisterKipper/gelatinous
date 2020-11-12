package gelatinous

import java.nio.file._
import java.text.SimpleDateFormat
import java.nio.file.attribute.BasicFileAttributes
import java.io.IOException

object Util {
  def convertStringToDate(s: String): java.util.Date = {
    val dateFormat = new SimpleDateFormat("yyyy-MM-dd")
    dateFormat.parse(s)
  }

  def Descending[T: Ordering]: scala.math.Ordering[T] = implicitly[Ordering[T]].reverse

  def deleteDirectoryRecursively(path: Path): Unit = {
    discard(
      Files.walkFileTree(
        path,
        new SimpleFileVisitor[Path] {
          override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
            Files.delete(file)
            FileVisitResult.CONTINUE
          }
          override def postVisitDirectory(dir: Path, exc: IOException): FileVisitResult = {
            Files.delete(dir)
            FileVisitResult.CONTINUE
          }
        }
      )
    )
  }

  @specialized
  def discard[A](evaluateForSideEffectOnly: A): Unit = {
    val _ = evaluateForSideEffectOnly
    ()
  }
}
