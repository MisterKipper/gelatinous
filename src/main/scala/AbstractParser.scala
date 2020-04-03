package gelatinous

abstract class AbstractParser {
  def parse(
      data: String,
  ): (Map[String, String], scalatags.Text.all.Frag, scalatags.Text.all.Frag)
}
