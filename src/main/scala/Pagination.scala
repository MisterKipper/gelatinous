package gelatinous

case class Pagination(items: List[FragmentTemplate], baseUrl: String, itemsPerPage: Int = 10) {
  val pageCount = 1 + (items.size - 1) / itemsPerPage

  def makePage(page: Int) = {
    import Text.all._
    // import Text.tags
    import Text.tags2.nav
    // import Text.attrs.href
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
