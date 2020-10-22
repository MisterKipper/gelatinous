package gelatinous

import scalatags.Text.all.Frag

trait Article {
  type Metadata = Map[String, String]
  val data: List[String]
  val parser: MarkdownParser = new MarkdownParser
  val (metadata: Map[String, String], postHtml: Frag, digest: Frag) = parser.parse(data)
  val route: String
  def render(): String
}
