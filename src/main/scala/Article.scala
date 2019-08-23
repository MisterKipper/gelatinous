package gelatinous

import scalatags.Text.all.Frag

abstract case class Article(data: List[String]) {
  type Metadata = Map[String, String]
  val route: String
  def processInput: (Metadata, Frag)
  def render: String
  val digest: Frag
}
