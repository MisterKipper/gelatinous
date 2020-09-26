package gelatinous.site

object AboutMe extends Base {
  import scalatags.Text.all._
  val pageTitle = "About Me"
  val route = "about-me.html"
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
