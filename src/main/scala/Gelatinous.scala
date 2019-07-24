package gelatinous

import java.io.IOException
import java.nio.file.{FileVisitResult, Files, Path, Paths, SimpleFileVisitor}
import java.nio.file.attribute.BasicFileAttributes

class Gelatinous(
    sourceDir: String = "src/main/resources",
    buildDir: String = "target/site",
    rootDir: String = "."
) {
  val sourcePath = Paths.get(rootDir, sourceDir)
  val buildPath = Paths.get(rootDir, buildDir)

  def build() = {

    cleanDirectory(buildPath)

    // val html = new BaseTemplate("Hello").pretty
    val template = new Template()
    val html = template.pretty

    val indexFile = buildPath.resolve("index.html")
    Files.createFile(indexFile)
    Files.write(indexFile, html.getBytes)

    Files.walkFileTree(
      sourcePath,
      new SimpleFileVisitor[Path] {
        override def visitFile(file: Path, attrs: BasicFileAttributes) =
          file match {
            case f if f.getFileName.toString.toLowerCase.endsWith(".md") =>
              processMarkdown(f)
            case f => processStatic(f)
          }
      }
    )
  }

  def processMarkdown(path: Path) = {
    println(f"$path")
    FileVisitResult.CONTINUE
  }

  def processStatic(path: Path) = {
    Files.copy(path, this.buildPath)
    FileVisitResult.CONTINUE
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
