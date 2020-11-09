package gelatinous
package site

object Blog extends Base with Index {
  import scalatags.Text.all._
  val route = "/blog"
  val pageTitle = "Blog"
  def pageContent(): Frag = {
    val digests = List(frag(h1("One"), p("Yeah that's right")), frag(h1("Two"), p("Test test")))
    val listItems = digests.map(li(cls := "post-digest")(_))
    ul(cls := "digests")(listItems)
  }
}
