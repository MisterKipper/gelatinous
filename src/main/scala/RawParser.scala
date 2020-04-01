package gelatinous

class RawParser extends AbstractParser {
  import scalatags.Text.all._
  def parse(data: String, nNodes: Int = 5) = (Map(), StringFrag(data), StringFrag(data))
}
