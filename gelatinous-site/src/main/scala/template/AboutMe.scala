package gelatinous.site.template

import scalatags.Text

object AboutMe extends Base {
  import Text.all._
  val pageTitle = "About Me"
  val route = "about-me.html"
  override def pageContent = {
    frag(
      h1("About me"),
      p(Text.attrs.cls := "First paragraph")(
        "This is my website, created using the static site generator I wrote using Scala."
      ),
      p("It's been interesting.")
    )
  }
}
