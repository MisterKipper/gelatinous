package gelatinous
package site

import scalatags.Text.all._

object AboutMe extends Base with Renderable {
  val pageTitle = "About Me"
  val route = "about-me"
  override def pageContent(): Frag = {
    frag(
      h1("About me"),
      p(
        "This is my website, created using the static site generator I wrote using Scala."
      ),
      p("It's been interesting.")
    )
  }
}
