package gelatinous

import scalatags.Text.all._

trait Template extends PrettyText {
  val titleAttr = scalatags.Text.attrs.title
  val title = scalatags.Text.tags2.title
  val main = scalatags.Text.tags2.main
  val nav = scalatags.Text.tags2.nav

  val myHtml: PrettyTypedTag
  def pageStyles(): Frag
  def pageContent(): Frag
  def pageScripts(): Frag
  def urlFor(folder: String, filename: String): String = {
    folder + '/' + filename
  }
}
