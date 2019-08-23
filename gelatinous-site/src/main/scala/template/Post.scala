package gelatinous.site.template

import scala.jdk.CollectionConverters._

import org.commonmark.parser.Parser
import org.commonmark.ext.front.matter.YamlFrontMatterExtension

import gelatinous.{Article, ArticleCollection, Index, PrettyText}
import gelatinous.site.Manifest
import gelatinous.site.util.ScalatagsVisitor
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor

class Post(data: List[String]) extends Article(data) with Base with PrettyText {
  val parser = Parser
    .builder()
    .extensions(List(YamlFrontMatterExtension.create()).asJava)
    .build()
  val mdParsed = parser.parse(data.reduce((result, line) => result + '\n' ++ line))
  def processInput = {
    val metadataVisitor = new YamlFrontMatterVisitor
    mdParsed.accept(metadataVisitor)
    val metadata = metadataVisitor.getData().asScala.view.mapValues(_.asScala(0)).toMap
    val postHtml = ScalatagsVisitor.walkTree(mdParsed)
    (metadata, postHtml)
  }
  val (metadata, postHtml) = processInput
  val digest = {
    ScalatagsVisitor.walkTree(mdParsed) // Some(3))
  }
  println(digest.render)
  val pageTitle = metadata("title")
  def pageContent = postHtml
  val slug = pageTitle.toLowerCase.replace(' ', '-')
  val route = PostCollection.baseRoute + slug + ".html"
}

object PostCollection extends ArticleCollection {
  val sourceDir = Manifest.sourceDir + "posts/"
  val baseRoute = "blog/"
  val indexPage = PostIndex
  def createArticle(lines: List[String]) = new Post(lines)
}

object PostIndex extends Base with Index {
  import scalatags.Text.all._
  val route = "index.html"
  val pageTitle = "Blog"
  def pageContent = PostCollection.articles.map(_.digest)
}
