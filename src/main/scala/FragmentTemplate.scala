package gelatinous

import scalatags.Text.all.Frag

trait FragmentTemplate extends PrettyText {
  val myHtml: Frag
  def render: String = myHtml.pretty
}
