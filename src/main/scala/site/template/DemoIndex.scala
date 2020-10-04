package gelatinous
package site

import scalatags.Text.all.Frag

object DemoIndex extends Base with Index {
  val route = "demos.html"
  val pageTitle = "Demos"
  def pageContent(): Frag =
    scalatags.Text.all.SeqFrag(
      DemoCollection.demos.toList.sortBy(_.metadata("timestamp"))(Util.Descending).map(_.summary)
    )
}
