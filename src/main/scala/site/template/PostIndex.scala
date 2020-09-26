package gelatinous.site

import gelatinous.Util.Descending
import gelatinous.site.PostCollection

object PostIndex extends Base with gelatinous.Index {
  import scalatags.Text.all._
  val route = "blog.html"
  val pageTitle = "Blog"
  def pageContent(): Frag = {
    val digests = PostCollection.articles.sortBy(_.timestamp)(Descending).map(_.digest)
    val listItems = digests.map(li(cls := "post-digest")(_))
    ul(cls := "digests")(listItems)
  }
}
