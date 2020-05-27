package gelatinous.site

import gelatinous.ArticleCollection

object PostCollection extends ArticleCollection[Post] {
  val sourceDir = s"${Manifest.sourceDir}posts"
  val baseRoute = "blog/"
  val indexPage = PostIndex
  def createArticle(lines: List[String]): Post = new Post(lines)
}
