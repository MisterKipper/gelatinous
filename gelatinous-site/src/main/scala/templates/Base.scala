package gelatinous.site

trait Base extends gelatinous.Template {
  import scalatags.Text.all._
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

  override def pageStyles(): Frag = {
    frag(
      link(rel := "stylesheet", href := urlFor("static", "css/styles.css")),
      link(rel := "stylesheet", href := urlFor("static", "css/fonts.css"))
    )
  }

  override def pageScripts(): Frag = {
    script(src := urlFor("static", "js/scripts.js"))
  }
}
