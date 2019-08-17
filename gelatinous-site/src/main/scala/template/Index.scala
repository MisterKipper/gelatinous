package gelatinous.site.template

// import gelatinous.site.Pagination
import scalatags.Text

class Index(posts: List[Digest]) extends Base {
  import Text.all._
  val pageTitle = "Home"
  val route = "index.html"
  // val pagination = Pagination(posts, "index")
  override def pageContent = {
    // pagination.makePage(0)
    val allHtml = posts.map(_.myHtml)
    ul(allHtml)
  }
}

object Index {
  def apply(posts: List[Digest]) = new Index(posts)
}
