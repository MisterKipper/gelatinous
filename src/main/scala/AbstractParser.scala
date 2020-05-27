package gelatinous

import scalatags.Text.all.Frag

abstract class AbstractParser {
  def parse(data: Seq[String]): (Map[String, String], Frag, Frag)
}
