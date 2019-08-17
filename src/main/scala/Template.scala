package gelatinous

import scalatags.Text.{Frag, TypedTag}

trait Template extends PrettyText {
  val myHtml: TypedTag[String]
  val route: String
  def pageStyles(): Frag
  def pageContent(): Frag
  def pageScripts(): Frag
  def urlFor(folder: String, filename: String): String = {
    s"$folder/$filename"
  }
  def render = myHtml.pretty
}
