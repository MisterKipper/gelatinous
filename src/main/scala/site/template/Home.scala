package gelatinous
package site

object Home extends Base with Renderable {
  import scalatags.Text.all._
  val pageTitle = "Home"
  val route = "/"
  lazy val pageContent: Frag = {
    val digests = List(frag(h1("One"), p("Yeah that's right")), frag(h1("Two"), p("Test test")))
    val listItems = digests.map(li(cls := "post-digest")(_))
    ul(cls := "digests")(listItems)
  }
}
