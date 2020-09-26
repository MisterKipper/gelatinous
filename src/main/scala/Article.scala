package gelatinous

import scalatags.Text.all.Frag

abstract class Article(data: List[String], parser: AbstractParser) {
  type Metadata = Map[String, String]
  val (metadata: Map[String, String], postHtml: Frag, digest: Frag) = parser.parse(data)
  val route: String
  def render: String
}
