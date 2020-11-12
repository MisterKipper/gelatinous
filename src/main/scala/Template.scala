package gelatinous

import scalatags.Text.all.Frag

trait Template extends PrettyText {
  val myHtml: Frag
  def pageStyles(): Frag
  def pageContent(): Frag
  def pageScripts(): Frag
  def urlFor(folder: String, filename: String): String = {
    s"$folder/$filename"
  }
  def build(metadata: ContentMetadata, content: Frag): String = ???
}
