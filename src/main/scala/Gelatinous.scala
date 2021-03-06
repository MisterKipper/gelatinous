package gelatinous

import java.io.IOException
import java.nio.file.{FileVisitResult, Files, Path, Paths, SimpleFileVisitor}
import java.nio.file.attribute.BasicFileAttributes

class Gelatinous(manifest: Manifest) {
  val sourcePath = Paths.get(manifest.sourceDir)
  val targetPath = Paths.get(manifest.targetDir)
  val assetSourcePath = Paths.get(manifest.assetDir)

  def build(): Unit = {
    cleanDirectory(targetPath)

    Files.createDirectories(targetPath)
    Files.walkFileTree(sourcePath.resolve(assetSourcePath), new AssetCopier(targetPath))

    manifest.standalonePages.foreach(
      page => {
        val html = page.render
        writeFile(html, page.route)
      }
    )

    manifest.collections.foreach(collection => {
      val html = collection.indexPage.render
      writeFile(html, collection.indexPage.route)
      collection.articles.foreach(article => {
        val html = article.render
        writeFile(html, article.route)
      })
    })
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

  private def writeFile(data: String, route: String): Unit = {
    val filePath = targetPath.resolve(Paths.get(route))
    Files.createDirectories(filePath.getParent)
    Files.createFile(filePath)
    Files.write(filePath, data.getBytes)
    ()
  }
}
