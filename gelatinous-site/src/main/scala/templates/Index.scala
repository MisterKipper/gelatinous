package gelatinous.site

object Index extends Base with Components {
  import scalatags.Text.all._
  val pageTitle = "Home"
  val posts: Seq[Map[String, String]] = ???
  override def pageContent = {
    frag(
      ul(posts.foreach(post => post("digest"))),
      pagination(posts)
    )
  }
}
