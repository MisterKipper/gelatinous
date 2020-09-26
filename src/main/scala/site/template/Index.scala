package gelatinous.site

import gelatinous.site.PostCollection

object Index extends Base {
  import scalatags.Text.all._
  val pageTitle = "Home"
  val route = "index.html"
  lazy val pageContent: Frag = {
    val digests = PostCollection.articles.sortBy(_.timestamp).map(_.digest)
    val listItems = digests.map(li(cls := "post-digest")(_))
    ul(cls := "digests")(listItems)
  }
}
