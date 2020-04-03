package gelatinous.site.template

object Index extends Base with gelatinous.Index {
  import scalatags.Text.all._
  val pageTitle = "Home"
  val route = "index.html"
  def pageContent = {
    val digests = PostCollection.articles.sortBy(_.timestamp).map(_.digest)
    val listItems = digests.map(li(cls := "post-digest")(_))
    ul(cls := "digests")(listItems)
  }
}
