package gelatinous

import java.nio.file.{FileVisitResult, Files, Path, SimpleFileVisitor}
import java.nio.file.attribute.BasicFileAttributes

class SourceVisitor(targetPath: Path) extends SimpleFileVisitor[Path] {
  override def visitFile(file: Path, attrs: BasicFileAttributes) = {
    file match {
      case f if checkFileEndsWith(f, ".md") => processMarkdown(f)
      case f => processStatic(f)
    }
    FileVisitResult.CONTINUE
  }

  def checkFileEndsWith(file: Path, extension: String) = {
    file.getFileName.toString.toLowerCase.endsWith(extension)
  }

  def processMarkdown(path: Path) = {
    println(f"$path")
  }

  def processStatic(path: Path) = {
    Files.copy(path, targetPath)
    FileVisitResult.CONTINUE
  }
}
