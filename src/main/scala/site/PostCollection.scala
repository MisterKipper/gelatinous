package gelatinous
package site

object PostCollection extends ArticleCollection[Post] {
  val sourceDir = s"${Site.sourceDir}posts"
  val baseRoute = "blog/"
  val indexPage = PostIndex
  def createArticle(lines: List[String]): Post = new Post(lines)
}
