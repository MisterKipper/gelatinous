package gelatinous

import scalatags.Text.all.Frag

case class Pagination(items: List[FragmentTemplate], baseUrl: String, itemsPerPage: Int = 10) {
  val pageCount = 1 + (items.size - 1) / itemsPerPage

  def makePage(page: Int): Frag = {
    import scalatags.Text.all._
    import scalatags.Text.tags2.nav
    import scalatags.Text.attrs.href
    frag(
      ul(items.slice((page - 1) * itemsPerPage, page * itemsPerPage).map(_.myHtml)),
      nav(
        ul(
          (1 to pageCount).map(i => li(a(href := s"$baseUrl/$i.html")(i.toString)))
        )
      )
    )
  }
}
