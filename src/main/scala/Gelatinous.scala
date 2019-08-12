package gelatinous

import java.io.IOException
import java.nio.file.{FileVisitResult, Files, Path, Paths, SimpleFileVisitor}
import java.nio.file.attribute.BasicFileAttributes

class Gelatinous(
    sourceDir: String,
    targetDir: String,
    manifest: Manifest
) {
  val sourcePath = Paths.get(sourceDir)
  val targetPath = Paths.get(targetDir)

  def build() = {
    cleanDirectory(targetPath)

    manifest.singlePages.foreach(
      page => {
        val html = page.myHtml.render // pretty
        val filePath = targetPath.resolve(page.url)
        Files.createFile(filePath)
        Files.write(filePath, html.getBytes)
      }
    )

    // Files.walkFileTree(sourcePath, new SourceVisitor(targetPath))
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
