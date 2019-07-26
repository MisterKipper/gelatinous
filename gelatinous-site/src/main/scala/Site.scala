package gelatinous.site

import java.nio.file.{Files, Paths}

import gelatinous._

object Site extends App {
  val sourceDir = "gelatinous-site/src/main/resources"
  val targetDir = "target/site"

  val sourcePath = Paths.get(sourceDir)
  val targetPath = Paths.get(targetDir)

  val html = Index.pretty

  val indexFile = targetPath.resolve("index.html")
  Files.createFile(indexFile)
  Files.write(indexFile, html.getBytes)
  val gelatinous = new Gelatinous(sourcePath, targetPath)
  gelatinous.build()
}
