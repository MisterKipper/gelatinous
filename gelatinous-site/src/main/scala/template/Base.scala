package gelatinous.site.template

import scalatags.Text

trait Base extends gelatinous.Template {
  import Text.all._
  val pageTitle: String
  lazy val myHtml = {
    html(lang := "en")(
      head(
        meta(charset := "utf-8"),
        Text.tags2.title(pageTitle + " - Kyle's junk"),
        link(rel := "shortcut icon", tpe := "image/png", href := urlFor("", "favicon.png")),
        link(rel := "icon", tpe := "image/png", href := urlFor("", "favicon.png")),
        meta(name := "viewport", content := "width=device-width, initial-scale=1.0"),
        pageStyles()
      ),
      body(
        header(
          Text.tags2.nav(
            ul(
              li(a(href := "/")("kyledavi.es")),
              li(a(href := "/blog.html")("Blog")),
              li(a(href := "/demos.html")("Demos")),
              li(a(href := "/about-me.html")("About Me"))
            )
          )
        ),
        Text.tags2.main(pageContent()),
        footer(RawFrag("&copy; 2019 Kyle F. Davies")),
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
