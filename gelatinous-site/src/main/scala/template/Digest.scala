package gelatinous.site.template

import gelatinous.Text

class Digest(post: Post) {
  import Text.all._
  val myHtml = frag(h2(post.pageTitle), p("TODO"), p("TODO"))
}
