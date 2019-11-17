package gelatinous.site.template

// import gelatinous.site.Pagination

object Index extends Base with gelatinous.Index {
  import scalatags.Text.all._
  val pageTitle = "Home"
  val route = "index.html"
  // val pagination = Pagination(posts, "index")
  def pageContent = PostCollection.articles
}
