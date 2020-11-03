package gelatinous

import scalatags.Text.all.Frag

trait Article {
  type Metadata = Map[String, String]
  val data: List[String]
  val (metadata: Map[String, String], postHtml: Frag, digest: Frag) = MarkdownParser.parse(data)
  val route: String
  def render(): String
}
