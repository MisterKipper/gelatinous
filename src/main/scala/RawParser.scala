package gelatinous

class RawParser extends AbstractParser {
  import scalatags.Text.all._
  def parse(data: Seq[String]): (Map[String, String], Frag, Frag) = {
    val s = data.reduce(_ + _)
    (Map(), StringFrag(s), StringFrag(s))
  }
}
