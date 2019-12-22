package gelatinous.site.template

object PostIndex extends Base with gelatinous.Index {
  import scalatags.Text.all._
  val route = "blog.html"
  val pageTitle = "Blog"
  def pageContent = {
    val digests = PostCollection.articles.sortBy(_.timestamp).map(_.digest)
    ul(li(digests(0)), li(digests(1)))
  }
}
