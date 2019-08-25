package gelatinous.site.template

import gelatinous.ArticleCollection
import gelatinous.site.Manifest

object PostCollection extends ArticleCollection {
  val sourceDir = Manifest.sourceDir + "posts/"
  val baseRoute = "blog/"
  val indexPage = PostIndex
  def createArticle(lines: List[String]) = new Post(lines)
}
