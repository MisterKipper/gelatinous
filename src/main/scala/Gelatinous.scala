package gelatinous

import java.io.IOException
import java.nio.file.{FileVisitResult, Files, Path, SimpleFileVisitor}
import java.nio.file.attribute.BasicFileAttributes

class Gelatinous(
    sourcePath: Path,
    targetPath: Path,
) {
  def build() = {
    cleanDirectory(targetPath)
    Files.walkFileTree(
      sourcePath,
      new SimpleFileVisitor[Path] {
        override def visitFile(file: Path, attrs: BasicFileAttributes) = {
          file match {
            case f if checkFileEndsWith(f, ".md") => processMarkdown(f)
            case f => processStatic(f)
          }
          FileVisitResult.CONTINUE
        }
      }
    )
  }

  def processMarkdown(path: Path) = {
    println(f"$path")
  }

  def processStatic(path: Path) = {
    Files.copy(path, this.targetPath)
    FileVisitResult.CONTINUE
  }

  private def checkFileEndsWith(file: Path, extension: String) = {
    file.getFileName.toString.toLowerCase.endsWith(extension)
  }

  private def cleanDirectory(dir: Path): Path = {
    if (Files.exists(dir)) {
      Files.walkFileTree(
        dir,
        new SimpleFileVisitor[Path] {
          override def visitFile(file: Path, attrs: BasicFileAttributes) = {
            Files.delete(file)
            FileVisitResult.CONTINUE
          }
          override def postVisitDirectory(dir: Path, exc: IOException) = {
            Files.delete(dir)
            FileVisitResult.CONTINUE
          }
        }
      )
    }
    Files.createDirectory(dir)
  }
}
