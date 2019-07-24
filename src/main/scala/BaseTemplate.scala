package gelatinous

import scalatags.Text.all._

abstract class BaseTemplate extends PrettyText {
  val titleAttr = scalatags.Text.attrs.title
  val title = scalatags.Text.tags2.title
  val main = scalatags.Text.tags2.main
  val nav = scalatags.Text.tags2.nav

  val pageTitle: String
  lazy val myHtml = {
    html(lang := "en")(
      head(
        meta(charset := "utf-8"),
        title(pageTitle + " - Kyle's junk"),
        link(rel := "shortcut icon", tpe := "image/png", href := urlFor("static", "favicon.png")),
        link(rel := "icon", tpe := "image/png", href := urlFor("static", "favicon.png")),
        meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
        pageStyles()
      ),
      body(
        header(
          nav(
            ul(
              li(a(href := "index.html")("kyledavi.es")),
              li(a(href := "blog.html")("Blog")),
              li(a(href := "demos.html")("Demos")),
              li(a(href := "about_me.html")("About Me"))
            )
          )
        ),
        main(
          h1(pageTitle),
          pageContent()
        ),
        footer("&copy; 2019 Kyle F. Davies"),
        pageScripts()
      )
    )
  }

  def pageStyles(): Frag = {
    frag(
      link(rel := "stylesheet", href := urlFor("static", "css/styles.css")),
      link(rel := "stylesheet", href := urlFor("static", "css/fonts.css"))
    )
  }

  def pageContent(): Frag
  def pageScripts(): Frag = {
    script(src := urlFor("static", "js/modals.js"))
  }
  def urlFor(folder: String, filename: String): String = {
    folder + '/' + filename
  }
}
