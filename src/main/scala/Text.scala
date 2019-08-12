package gelatinous

import java.util.Objects

import scalatags.generic
import scalatags.stylesheet
import scalatags.text
import scalatags.{Companion, DataConverters, Escaping}
import scalatags.generic.{Aliases, Namespace, StylePair}
import scala.annotation.unchecked.uncheckedVariance
import scalatags.stylesheet.{StyleSheetFrag, StyleTree}

/**
  * A Scalatags module that works with a text back-end, i.e. it creates HTML
  * `String`s.
  */
object Text
    extends generic.Bundle[Builder, String, String]
    with Aliases[Builder, String, String] {

  object attrs extends Text.Cap with Attrs
  object tags extends Text.Cap with text.Tags
  object tags2 extends Text.Cap with text.Tags2
  object styles extends Text.Cap with Styles
  object styles2 extends Text.Cap with Styles2

  object svgTags extends Text.Cap with text.SvgTags
  object svgAttrs extends Text.Cap with SvgAttrs

  object implicits extends Aggregate with DataConverters

  object all extends Cap with Attrs with Styles with text.Tags with DataConverters with Aggregate

  object short extends Cap with text.Tags with DataConverters with Aggregate with AbstractShort {
    object * extends Cap with Attrs with Styles
  }

  trait Cap extends Util with text.TagFactory { self =>
    type ConcreteHtmlTag[T <: String] = TypedTag[T]
    type BaseTagType = TypedTag[String]
    implicit protected[this] def stringAttrX = new GenericAttr[String]
    implicit protected[this] def stringStyleX = new GenericStyle[String]
    implicit protected[this] def stringPixelStyleX = new GenericPixelStyle[String](stringStyleX)
    implicit def UnitFrag(u: Unit) = new StringFrag("")
    def makeAbstractTypedTag[T](tag: String, void: Boolean, namespaceConfig: Namespace) = {
      TypedTag(tag, Nil, void)
    }
    implicit class SeqFrag[A](xs: Seq[A])(implicit ev: A => Frag) extends Frag {
      Objects.requireNonNull(xs)
      def applyTo(t: Builder) = xs.foreach(_.applyTo(t))
      def render = xs.map(_.render).mkString
    }
  }

  trait Aggregate extends generic.Aggregate[Builder, String, String] {
    implicit def ClsModifier(s: stylesheet.Cls): Modifier =
      new Modifier with Builder.ValueSource {
        def applyTo(t: Builder) = t.appendAttr("class", this)

        override def appendAttrValue(sb: StringBuilder): Unit = {
          Escaping.escape(s.name, sb)
        }
      }

    implicit class StyleFrag(s: generic.StylePair[Builder, _]) extends StyleSheetFrag {
      def applyTo(c: StyleTree) = {
        val b = new Builder()
        s.applyTo(b)
        val Array(style, value) = b.attrsString(b.attrs(b.attrIndex("style"))._2).split(":", 2)
        c.copy(styles = c.styles.updated(style, value))
      }
    }

    def genericAttr[T] = new GenericAttr[T]
    def genericStyle[T] = new GenericStyle[T]
    def genericPixelStyle[T](implicit ev: StyleValue[T]): PixelStyleValue[T] =
      new GenericPixelStyle[T](ev)
    def genericPixelStylePx[T](implicit ev: StyleValue[String]): PixelStyleValue[T] =
      new GenericPixelStylePx[T](ev)

    implicit def stringFrag(v: String) = new StringFrag(v)

    val RawFrag = RawFrag
    val StringFrag = StringFrag
    type StringFrag = Text.StringFrag
    type RawFrag = Text.RawFrag
    def raw(s: String) = RawFrag(s)
    val Tag = TypedTag
  }

  case class StringFrag(v: String) extends Frag {
    Objects.requireNonNull(v)
    def render = {
      val strb = new StringBuilder()
      writeTo(strb, 0)
      strb.toString()
    }
    def writeTo(strb: StringBuilder, depth: Int) = {
      println(depth)
      Escaping.escape(v, strb)
    }
  }

  object StringFrag extends Companion[StringFrag]
  object RawFrag extends Companion[RawFrag]
  case class RawFrag(v: String) extends Frag {
    Objects.requireNonNull(v)
    def render = v
    def writeTo(strb: StringBuilder, depth: Int) = {
      println(depth)
      strb ++= v
    }
  }

  class GenericAttr[T] extends AttrValue[T] {
    def apply(t: Builder, a: Attr, v: T): Unit = {
      t.setAttr(a.name, Builder.GenericAttrValueSource(v.toString))
    }
  }

  class GenericStyle[T] extends StyleValue[T] {
    def apply(t: Builder, s: Style, v: T): Unit = {
      t.appendAttr("style", Builder.StyleValueSource(s, v.toString))
    }
  }

  class GenericPixelStyle[T](ev: StyleValue[T]) extends PixelStyleValue[T] {
    def apply(s: Style, v: T) = StylePair(s, v, ev)
  }

  class GenericPixelStylePx[T](ev: StyleValue[String]) extends PixelStyleValue[T] {
    def apply(s: Style, v: T) = {
      StylePair(s, v.toString + "px", ev)
    }
  }

  case class TypedTag[+Output <: String](
      tag: String = "",
      modifiers: List[Seq[Modifier]],
      void: Boolean = false
  ) extends generic.TypedTag[Builder, Output, String]
      with Frag {
    // unchecked because Scala 2.10.4 seems to not like this, even though
    // 2.11.1 works just fine. I trust that 2.11.1 is more correct than 2.10.4
    // and so just force this.
    protected[this] type Self = TypedTag[Output @uncheckedVariance]

    /**
      * Serialize this [[TypedTag]] and all its children out to the given StringBuilder.
      *
      * Although the external interface is pretty simple, the internals are a huge mess,
      * because I've inlined a whole lot of things to improve the performance of this code
      * ~4x from what it originally was, which is a pretty nice speedup
      */
    def writeTo(strb: StringBuilder, depth: Int) = {
      val builder = new Builder()
      build(builder)

      val indent = "  " * depth
      strb += '\n' ++= indent

      // tag
      strb += '<' ++= tag

      // attributes
      var i = 0
      while (i < builder.attrIndex) {
        val pair = builder.attrs(i)
        strb += ' ' ++= pair._1 ++= "=\""
        builder.appendAttrStrings(pair._2, strb)
        strb += '\"'
        i += 1
      }

      if (builder.childIndex == 0) {
        if (void) {
          // No children - self-closing tag
          strb ++= " />"
        } else {
          // No children - close tag
          strb ++= "></" ++= tag += '>'
        }
      } else {
        // Children
        var i = 0
        while (i < builder.childIndex) {
          builder.children(i).writeTo(strb, depth + 1)
          i += 1
        }
        strb += '\n' ++= indent
        // Closing tag
        strb ++= "</" ++= tag += '>'
      }
      ()
    }

    def apply(xs: Modifier*): TypedTag[Output] = {
      this.copy(tag = tag, void = void, modifiers = xs :: modifiers)
    }

    /**
      * Converts an ScalaTag fragment into an html string
      */
    override def toString = {
      val strb = new StringBuilder
      writeTo(strb, 0)
      strb.toString()
    }
    def render: Output = this.toString.asInstanceOf[Output]
  }

}

// import scala.reflect.ClassTag
import scalatags.generic.Style

/**
 * Object to aggregate the modifiers into one coherent data structure
 * so the final HTML string can be properly generated. It's really
 * gross internally, but bloody fast. Even using pre-built data structures
 * like `mutable.Buffer` slows down the benchmarks considerably. Also
 * exposes more of its internals than it probably should for performance,
 * so even though the stuff isn't private, don't touch it!
 */
class Builder(var children: Array[Frag] = new Array(4),
              var attrs: Array[(String, Builder.ValueSource)] = new Array(4)){
  final var childIndex = 0
  final var attrIndex = 0

  private[this] def incrementChidren(arr: Array[Frag], index: Int) = {
    if (index >= arr.length){
      val newArr = new Array[Frag](arr.length * 2)
      var i = 0
      while(i < arr.length){
        newArr(i) = arr(i)
        i += 1
      }
      newArr
    }else{
      null
    }
  }

  private[this] def incrementAttr(arr: Array[(String, Builder.ValueSource)], index: Int) = {
    if (index >= arr.length){
      val newArr = new Array[(String, Builder.ValueSource)](arr.length * 2)
      var i = 0
      while(i < arr.length){
        newArr(i) = arr(i)
        i += 1
      }
      newArr
    }else{
      null
    }
  }

  // private[this] def increment[T: ClassTag](arr: Array[T], index: Int) = {
  //   if (index >= arr.length){
  //     val newArr = new Array[T](arr.length * 2)
  //     var i = 0
  //     while(i < arr.length){
  //       newArr(i) = arr(i)
  //       i += 1
  //     }
  //     newArr
  //   }else{
  //     null
  //   }
  // }
  def addChild(c: Frag) = {
    val newChildren = incrementChidren(children, childIndex)
    if (newChildren != null) children = newChildren
    children(childIndex) = c
    childIndex += 1
  }
  def appendAttr(k: String, v: Builder.ValueSource) = {

    attrIndex(k) match{
      case -1 =>
        val newAttrs = incrementAttr(attrs, attrIndex)
        if (newAttrs!= null) attrs = newAttrs

        attrs(attrIndex) = k -> v
        attrIndex += 1
      case n =>
        val (oldK, oldV) = attrs(n)
        attrs(n) = (oldK, Builder.ChainedAttributeValueSource(oldV, v))
    }
  }
  def setAttr(k: String, v: Builder.ValueSource) = {
    attrIndex(k) match{
      case -1 =>
        val newAttrs = incrementAttr(attrs, attrIndex)
        if (newAttrs!= null) attrs = newAttrs
        attrs(attrIndex) = k -> v
        attrIndex += 1
      case n =>
        val (oldK, oldV) = attrs(n)
        attrs(n) = (oldK, Builder.ChainedAttributeValueSource(oldV, v))
    }
  }


  def appendAttrStrings(v: Builder.ValueSource, sb: StringBuilder): Unit = {
    v.appendAttrValue(sb)
  }

  def attrsString(v: Builder.ValueSource): String = {
    val sb = new StringBuilder
    appendAttrStrings(v, sb)
    sb.toString
  }



  def attrIndex(k: String): Int = {
    attrs.indexWhere(x => x != null && x._1 == k)
  }
}

object Builder{

  /**
    * More-or-less internal trait, used to package up the parts of a textual
    * attribute or style so that we can append the chunks directly to the
    * output buffer. Improves perf over immediately combining them into a
    * string and storing that, since this avoids allocating that intermediate
    * string.
    */
  trait ValueSource {
    def appendAttrValue(strb: StringBuilder): Unit
  }
  case class StyleValueSource(s: Style, v: String) extends ValueSource {
    override def appendAttrValue(strb: StringBuilder): Unit = {
      Escaping.escape(s.cssName, strb)
      strb ++=  ": "
      Escaping.escape(v, strb)
      strb ++= ";"
    }
  }

  case class GenericAttrValueSource(v: String) extends ValueSource {
    override def appendAttrValue(strb: StringBuilder): Unit = {
      Escaping.escape(v, strb)
    }
  }

  case class ChainedAttributeValueSource(head: ValueSource, tail: ValueSource) extends ValueSource {
    override def appendAttrValue(strb: StringBuilder): Unit = {
      head.appendAttrValue(strb)
      strb ++= " "
      tail.appendAttrValue(strb)
    }
  }
}

trait Frag extends generic.Frag[Builder, String]{
  def writeTo(strb: StringBuilder, depth: Int): Unit
  def render: String
  def applyTo(b: Builder) = b.addChild(this)
}
