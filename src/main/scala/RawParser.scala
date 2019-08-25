package gelatinous

class RawParser extends AbstractParser {
  import scalatags.Text.all._
  def parse(data: String) = (Map(), StringFrag(data))
}
