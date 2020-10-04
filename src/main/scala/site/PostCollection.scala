package gelatinous
package site

object PostCollection extends ArticleCollection[Post] {
  lazy val sourceDir = s"${Site.manifest.sourceDir}posts"
  val baseRoute = "blog/"
  val indexPage = PostIndex
  def createArticle(lines: List[String]): Post = new Post(lines)
}
