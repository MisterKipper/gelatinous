package gelatinous

// import scalatags.Text.all.Frag

abstract class Article(data: List[String], parser: AbstractParser = new RawParser) {
  type Metadata = Map[String, String]
  val (metadata, postHtml, digest) = parser.parse(data)
  val route: String
  // def parse(data: String): (Metadata, Frag, Frag) = parser.parse(data)
  def render: String
  // val digest: Frag
}
