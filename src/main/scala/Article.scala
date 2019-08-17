package gelatinous

import scalatags.Text

abstract case class Article(data: List[String]) {
  val route: String
  processInput(data)
  def processInput(data: List[String]): Text.Frag
  def render: String
  def digest: Text.Frag
}
