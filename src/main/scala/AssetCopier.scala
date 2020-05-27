package gelatinous

import java.nio.file.{FileVisitResult, Files, Path, SimpleFileVisitor}
import java.nio.file.attribute.BasicFileAttributes

class AssetCopier(targetPath: Path) extends SimpleFileVisitor[Path] {
  var sourcePath: Option[Path] = None
  override def preVisitDirectory(dir: Path, attrs: BasicFileAttributes): FileVisitResult = {
    sourcePath match {
      case None => sourcePath = Some(dir)
      case p => Files.createDirectories(targetPath.resolve(p.get.relativize(dir)))
    }
    FileVisitResult.CONTINUE
  }

  override def visitFile(file: Path, attrs: BasicFileAttributes): FileVisitResult = {
    Files.copy(file, targetPath.resolve(sourcePath.get.relativize(file)))
    FileVisitResult.CONTINUE
  }
}
