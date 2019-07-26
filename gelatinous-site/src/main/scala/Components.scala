package gelatinous.site

import gelatinous.Template

trait Components extends Template {
  import scalatags.Text.all._
  def pagination(items: Seq[_], itemsPerPage: Int = 10) = {
    val pageCount = 1 + items.size / itemsPerPage
    nav(
      ul(
        frag(for (i <- 1 to pageCount) li(i))
      )
    )
  }
}
