package gelatinous

abstract class AbstractParser {
  def parse(
      data: String,
      nNodes: Int = 5
  ): (Map[String, String], scalatags.Text.all.Frag, scalatags.Text.all.Frag)
}
