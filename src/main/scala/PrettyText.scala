package gelatinous

import scalatags.Text.TypedTag
import scalatags.text.Builder

@SuppressWarnings(
  Array(
    "org.wartremover.warts.Var",
    "org.wartremover.warts.NonUnitStatements",
    "org.wartremover.warts.While",
    "org.wartremover.warts.Recursion"
  )
)
trait PrettyText {
  implicit class PrettyTypedTag(tt: scalatags.Text.TypedTag[String]) {
    def prettyWriteTo(strb: java.io.Writer, depth: Int): java.io.Writer = {
      val builder = new Builder()
      tt.build(builder)
      val indent = "  " * depth
      strb.append(indent).append('<').append(tt.tag)

      var i = 0
      while (i < builder.attrIndex) {
        val pair = builder.attrs(i)
        strb.append(' ').append(pair._1).append("=\"")
        builder.appendAttrStrings(pair._2, strb)
        strb.append('\"')
        i += 1
      }

      if (builder.childIndex === 0 && tt.void) {
        strb.append(" />")
      } else {
        strb.append(">")
        var i = 0
        while (i < builder.childIndex) {
          builder.children(i) match {
            case t: scalatags.Text.all.StringFrag if t.v.matches("^\\s*$") => ()
            case t: TypedTag[String] =>
              strb.append('\n')
              t.prettyWriteTo(strb, depth + 1)
            case any =>
              strb.append('\n').append("  " * (depth + 1))
              any.writeTo(strb)
          }
          i += 1
        }
        strb.append('\n').append(indent).append("</").append(tt.tag).append('>')
      }
    }

    def pretty: String = {
      val strb = new java.io.StringWriter
      prettyWriteTo(strb, 0)
      strb.toString()
    }
  }

  implicit class PrettyFrag(f: scalatags.Text.all.Frag) {
    def pretty: String = f match {
      case f: TypedTag[String] => f.pretty
      case f: Any => f.render
    }
  }

  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  implicit final class AnyOps[A](self: A) {
    def ===(other: A): Boolean = self == other
  }
}
