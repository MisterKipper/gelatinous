package gelatinous

abstract class Article(data: List[String], parser: AbstractParser = new RawParser) {
  type Metadata = Map[String, String]
  val (metadata, postHtml, digest) = parser.parse(data)
  val route: String
  def render: String
}
