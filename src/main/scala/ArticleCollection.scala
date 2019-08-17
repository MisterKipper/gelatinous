package gelatinous

import java.nio.file.{Files, Paths}
import scala.jdk.CollectionConverters._

trait ArticleCollection {
  val sourceDir: String
  val baseRoute: String
  lazy val sourcePath = Paths.get(sourceDir)
  val indexPage: Index
  lazy val articles = getArticles().toList
  def getArticles(): Iterator[Article] = {
    Files
      .list(sourcePath)
      .iterator
      .asScala
      .map(file => {
        createArticle(Files.readAllLines(file).asScala.toList)
      })
  }
  def createArticle(lines: List[String]): Article
}
