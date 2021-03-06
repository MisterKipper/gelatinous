package gelatinous.site

import java.nio.file.Files
import scala.jdk.CollectionConverters._
import gelatinous.{ArticleCollection, MarkdownParser}

object DemoCollection extends ArticleCollection[Demo] {
  val sourceDir = Manifest.sourceDir ++ "demos/"
  val baseRoute = "demos/"
  val indexPage = DemoIndex
  val demos = Files
    .list(sourcePath)
    .iterator
    .asScala
    .map(file => createArticle(Files.readAllLines(file).asScala.toList))
  def createArticle(lines: List[String]): Demo = {
    val (metadata, _, summary) = new MarkdownParser().parse(lines)
    Demo(metadata, summary)
  }
}
