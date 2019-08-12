package gelatinous.site.template

// import gelatinous.site.Pagination

class Index(posts: List[Digest]) extends Base {
  import gelatinous.Text.all._
  val pageTitle = "Home"
  val url = "index.html"
  // val pagination = Pagination(posts, "index")
  override def pageContent = {
    // pagination.makePage(0)
    ul(posts.map(_.myHtml))
  }
}

object Index {
  def apply(posts: List[Digest]) = new Index(posts)
}
