package gelatinous

import scala.annotation.tailrec
import scala.collection.mutable.{Map => MutableMap}
import scala.jdk.CollectionConverters._

import org.commonmark.node
import org.commonmark.parser.Parser
import org.commonmark.ext.front.matter.YamlFrontMatterBlock
import org.commonmark.ext.front.matter.YamlFrontMatterNode
import org.commonmark.ext.front.matter.YamlFrontMatterExtension

import Util.discard

// TODO: Rewrite to be properly functional and not have to ignore all these warts.
@SuppressWarnings(Array("org.wartremover.warts.Var", "org.wartremover.warts.MutableDataStructures", "org.wartremover.warts.DefaultArguments"))
object MarkdownParser {
  import scalatags.Text.all._
  private val parser: Parser = Parser
    .builder()
    .extensions(List(YamlFrontMatterExtension.create()).asJava)
    .build()
  private val maxNodes = 3
  private var nNodes = 0
  val metadata: MutableMap[String, String] = MutableMap()

  def parse(markdown: Seq[String]): (Map[String, String], Frag, Frag) = {
    val s = markdown.fold("")((acc, line) => acc ++ "\n" ++ line)
    val mdParsed = parser.parse(s)
    val postHtml = MarkdownParser.walkTree(mdParsed)
    val metadata = MarkdownParser.metadata.toMap
    MarkdownParser.nNodes = 0
    val digest = MarkdownParser.walkTree(mdParsed, true)
    (metadata, postHtml, digest)
  }

  def walkTree(n: node.Node, isDigest: Boolean = false): Frag = {
    if (isDigest && maxNodes - nNodes === 0) {
      ""
    } else {
      convertNode(n, isDigest)
    }
  }

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def convertNode(n: node.Node, isDigest: Boolean): Frag = {
    n match {
      case null => UnitFrag(())
      case n: node.Text => {
        if (isDigest) {
          nNodes += 1
        }
        n.getLiteral()
      }
      case _: node.SoftLineBreak => " "
      case n: node.Image => img(src := n.getDestination, title := n.getTitle)
      case n: YamlFrontMatterNode => {
        discard(metadata += (n.getKey() -> n.getValues().get(0)))
        // UnitFrag(())
      }
      case n: YamlFrontMatterBlock => {
        getChildren(n) match {
          case Some(c) => c.map(walkTree(_, isDigest))
          case None => UnitFrag(())
        }
      }
      case n: Any => convertBranchNode(n, isDigest)
    }
  }

  def convertBranchNode(n: node.Node, isDigest: Boolean): Frag = {
    val f = n match {
      case _: node.Document => scalatags.Text.tags2.article(_)
      case n: node.Heading => convertHeading(n, isDigest)
      case _: node.Paragraph => p(_)
      case _: node.BulletList => ul(_)
      case _: node.ListItem => li(_)
      case _: node.Emphasis => em(_)
      case _: node.BlockQuote => blockquote(_)
      case _: node.Code => code(_)
      case _: node.HardLineBreak => br(_)
      case _: node.OrderedList => ol(_)
      case _: node.StrongEmphasis => b(_)
      case _: node.ThematicBreak => hr(_)
      case n: node.Link => a(href := n.getDestination(), title := n.getTitle())(_)
      case n: Any => {
        println(s"Unknown commonmark node type: ${n.toString()}")
        tag(s"${n.toString().dropRight(2)}")(_)
      }
    }

    val x = getChildren(n) match {
      case Some(c) => c.map(walkTree(_, isDigest)).fold(frag())(frag(_, _))
      case None => UnitFrag(())
    }

    def g(xs: Modifier*): Frag = {
      f.apply(xs)
    }
    g(x)
  }

  def convertHeading(n: node.Heading, isDigest: Boolean): (Seq[Modifier] => Frag) = {
      if (isDigest) {
        val x = n.getLevel() match {
          case 1 =>
            (x: Seq[scalatags.Text.Modifier]) =>
              h2(a(href := s"""/blog/${this
                .metadata("title")
                .toLowerCase
                .replace(' ', '-')}.html""")(x))
          case 2 => h3(_)
          case 3 => h4(_)
          case 4 => h5(_)
          case 5 => h6(_)
          case 6 => h6(_)
        }
        x
      } else {
        n.getLevel() match {
          case 1 => h1(_)
          case 2 => h2(_)
          case 3 => h3(_)
          case 4 => h4(_)
          case 5 => h5(_)
          case 6 => h6(_)
        }
      }
  }

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  def getChildren(n: node.Node): Option[List[node.Node]] = n match {
    case null => None
    case n => {
      Some(getSiblings(n.getFirstChild, List()))
    }
  }

  @SuppressWarnings(Array("org.wartremover.warts.Null"))
  @tailrec
  def getSiblings(n: node.Node, result: List[node.Node]): List[node.Node] = n match {
    case null => result.reverse
    case n => getSiblings(n.getNext(), n :: result)
  }

  @SuppressWarnings(Array("org.wartremover.warts.Equals"))
  implicit final class AnyOps[A](self: A) {
    def ===(other: A): Boolean = self == other
  }
}
