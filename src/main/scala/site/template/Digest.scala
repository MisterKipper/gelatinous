package gelatinous.site.template

class Digest(post: Post) {
  import scalatags.Text.all._
  val myHtml = frag(h2(post.pageTitle), p("TODO"), p("TODO"))
}
