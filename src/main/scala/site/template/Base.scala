package gelatinous
package site

import scalatags.Text.all._
import scalatags.Text.tags2

trait Base extends Template {
  val pageTitle: String
  lazy val myHtml = {
    html(lang := "en")(
      head(
        meta(charset := "utf-8"),
        tags2.title(pageTitle + " - Kyle's junk"),
        link(rel := "shortcut icon", tpe := "image/png", href := urlFor("", "favicon.png")),
        link(rel := "icon", tpe := "image/png", href := urlFor("", "favicon.png")),
        meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
        pageStyles()
      ),
      body(
        header(
          tags2.nav(
            ul(
              li(a(href := "/")("www.kyle.earth")),
              li(a(href := "/blog")("Blog")),
              li(a(href := "/demo")("Demos")),
              li(a(href := "/about-me")("About Me"))
            )
          )
        ),
        tags2.main(pageContent()),
        footer(RawFrag("&copy; 2020 Kyle F. Davies")),
        pageScripts()
      )
    )

  }

  override def pageStyles(): Frag = {
    frag(
      link(rel := "stylesheet", href := urlFor("/css", "styles.css")),
      link(rel := "stylesheet", href := urlFor("/css", "fonts.css"))
    )
  }

  override def pageScripts(): Frag = {
    script(src := urlFor("/js", "scripts.js"))
  }
}
