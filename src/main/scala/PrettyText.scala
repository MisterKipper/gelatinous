package gelatinous

import scalatags.Text.TypedTag
import scalatags.text.Builder

trait PrettyText {
  val myHtml: PrettyTypedTag

  implicit class PrettyTypedTag(tt: TypedTag[String]) {
    def prettyWriteTo(strb: StringBuilder, depth: Int): StringBuilder = {
      val builder = new Builder()
      tt.build(builder)
      val indent = "  " * depth
      strb ++= indent += '<' ++= tt.tag

      var i = 0
      while (i < builder.attrIndex) {
        val pair = builder.attrs(i)
        strb += ' ' ++= pair._1 ++= "=\""
        builder.appendAttrStrings(pair._2, strb)
        strb += '\"'
        i += 1
      }

      if (builder.childIndex == 0 && tt.void) {
        strb ++= " />"
      } else {
        strb ++= ">"
        var i = 0
        while (i < builder.childIndex) {
          builder.children(i) match {
            case t: PrettyTypedTag =>
              strb += '\n'
              t.prettyWriteTo(strb, depth + 1)
            case any =>
              strb += '\n' ++= "  " * (depth + 1)
              any.writeTo(strb)
          }
          i += 1
        }
        strb += '\n' ++= indent ++= "</" ++= tt.tag += '>'
      }
    }

    def pretty: String = {
      val strb = new StringBuilder
      prettyWriteTo(strb, 0)
      strb.toString()
    }
  }

  def pretty: String = {
    "<!DOCTYPE html>\n" + myHtml.pretty
  }
}
