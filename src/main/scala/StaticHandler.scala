package gelatinous

import java.nio.file.Path
import java.nio.file.Files
import java.nio.file.SimpleFileVisitor
import java.nio.file.FileVisitResult
import java.nio.file.attribute.BasicFileAttributes

@SuppressWarnings(Array("org.wartremover.warts.StringPlusAny"))
class StaticHandler(source: Path) extends Handler {
  def writeContents(target: Path): Unit = {
    Util.discard(
      Files.walkFileTree(
        source,
        new SimpleFileVisitor[Path] {
          override def visitFile(path: Path, attrs: BasicFileAttributes): FileVisitResult = {
            val targetFile = target.resolve(source.relativize(path))
            Util.discard(Files.copy(path, targetFile))
            FileVisitResult.CONTINUE
          }

          override def preVisitDirectory(path: Path, attrs: BasicFileAttributes): FileVisitResult = {
            val dir = target.resolve(source.relativize(path))
            if (!Files.exists(dir)) Util.discard(Files.createDirectory(dir))
            FileVisitResult.CONTINUE
          }
        }
      )
    )
  }
}
