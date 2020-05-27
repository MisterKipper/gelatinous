package gelatinous.site

import gelatinous.site.PostCollection

object Index extends Base with gelatinous.Index {
  import scalatags.Text.all._
  val pageTitle = "Home"
  val route = "index.html"
  def pageContent: Frag = {
    val digests = PostCollection.articles.sortBy(_.timestamp).map(_.digest)
    val listItems = digests.map(li(cls := "post-digest")(_))
    ul(cls := "digests")(listItems)
  }
}
