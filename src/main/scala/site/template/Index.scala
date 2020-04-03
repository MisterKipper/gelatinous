package gelatinous.site.template

object Index extends Base with gelatinous.Index {
  import scalatags.Text.all._
  val pageTitle = "Home"
  val route = "index.html"
  def pageContent = {
    val digests = PostCollection.articles.sortBy(_.timestamp).map(_.digest)
    // ul(cls := "digests")(li(digests(0)), li(digests(1)))
    val digests1 = digests.map(li(cls := "post-digest")(_))
    ul(cls := "digests")(digests1)
  }
}
