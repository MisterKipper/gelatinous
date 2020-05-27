package gelatinous.site

import scalatags.Text.all.Frag

import gelatinous.Util.Descending
import gelatinous.site.DemoCollection

object DemoIndex extends Base with gelatinous.Index {
  val route = "demos.html"
  val pageTitle = "Demos"
  def pageContent: Frag =
    scalatags.Text.all.SeqFrag(
      DemoCollection.demos.toList.sortBy(_.metadata("timestamp"))(Descending).map(_.summary)
    )
}
