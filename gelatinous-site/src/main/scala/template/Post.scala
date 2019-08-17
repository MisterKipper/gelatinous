package gelatinous.site.template

import scalatags.Text

import gelatinous.{ArticleCollection, Article, Index, PrettyText}
import gelatinous.site.Manifest

class Post(data: List[String]) extends Article(data) with PrettyText {
  import Text.all._
  import Text.tags2.article
  val htmlFrag = processInput(data)
  val pageTitle = data(3).split(": ")(1)
  val route = PostCollection.baseRoute + slug
  def slug = pageTitle.toLowerCase.replace(' ', '-') + ".html"
  def render = htmlFrag.pretty
  def processInput(lines: List[String]) = article(h2(data(0)), p(data(1)), p(data(2)))
  def digest: Frag = {
    article(h2("TODO"), p("TODO"), p("TODO"))
  }
}

object PostCollection extends ArticleCollection {
  val sourceDir = Manifest.sourceDir + "posts/"
  val baseRoute = "blog/"
  val indexPage = PostIndex
  def createArticle(lines: List[String]) = new Post(lines)
}

object PostIndex extends Base with Index {
  import scalatags.Text.all._
  val route = "blog.html"
  val pageTitle = "Blog"
  def pageContent = PostCollection.articles.map(_.digest)
}
