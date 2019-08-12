package gelatinous

import Text.Frag

trait Template {
  val myHtml: Frag // PrettyTypedTag
  val url: String
  def render = myHtml.render
  def pageStyles(): Frag
  def pageContent(): Frag
  def pageScripts(): Frag
  def urlFor(folder: String, filename: String): String = {
    s"$folder/$filename"
  }
}
