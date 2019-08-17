package gelatinous

import scalatags.Text.TypedTag

trait FragmentTemplate extends PrettyText {
  val myHtml: TypedTag[String]
  def render = myHtml.pretty
}
