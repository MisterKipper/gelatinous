package gelatinous.site.template

import gelatinous.Text

class AboutMe extends Base {
  import Text.all._
  val pageTitle = "About Me"
  val url = "about-me.html"
  override def pageContent = {
    frag(
      h1("About me"),
      // p(Text.attrs.cls := "First paragraph")(
      //   "This is my website, created using the static site generator I wrote using Scala."
      // ),
      p("It's been interesting.")
    )
  }
}

object AboutMe extends AboutMe {}
