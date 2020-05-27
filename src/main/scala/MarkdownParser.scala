package gelatinous

import scala.collection.mutable.{Map => MutableMap}
import scala.jdk.CollectionConverters._

import org.commonmark.node
import org.commonmark.parser.Parser
import org.commonmark.ext.front.matter.YamlFrontMatterBlock
import org.commonmark.ext.front.matter.YamlFrontMatterNode
import org.commonmark.ext.front.matter.YamlFrontMatterExtension
import scalatags.Text.all.Frag

class MarkdownParser extends AbstractParser {
  val parser = Parser
    .builder()
    .extensions(List(YamlFrontMatterExtension.create()).asJava)
    .build()
  def parse(markdown: Seq[String]): (Map[String, String], Frag, Frag) = {
    val s = markdown.reduce((result, line) => result + '\n' ++ line)
    val mdParsed = parser.parse(s)
    val postHtml = MarkdownParser.walkTree(mdParsed)
    val metadata = MarkdownParser.metadata.toMap
    MarkdownParser.nNodes = 0
    val digest = MarkdownParser.walkTree(mdParsed, true)
    (metadata, postHtml, digest)
  }
}

object MarkdownParser {
  import scalatags.Text.all._
  private val maxNodes = 3
  private var nNodes = 0
  val metadata: MutableMap[String, String] = MutableMap()
  def walkTree(n: node.Node, isDigest: Boolean = false): Frag = {
    if (isDigest && maxNodes - nNodes == 0) {
      ""
    } else {
      convertNode(n, isDigest)
    }
  }

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
        metadata += (n.getKey() -> n.getValues().get(0))
        UnitFrag(())
      }
      case n: YamlFrontMatterBlock => {
        getChildren(n).get.map(walkTree(_, isDigest))
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
        println(s"Unknown commonmark node type: $n")
        tag(s"${n.toString().dropRight(2)}")(_)
      }
    }
    val x = getChildren(n).get.map(walkTree(_, isDigest)).fold(frag())(frag(_, _))
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

  def getChildren(n: node.Node): Option[List[node.Node]] = n match {
    case null => None
    case n => {
      Some(getSiblings(n.getFirstChild, List()))
    }
  }

  def getSiblings(n: node.Node, result: List[node.Node]): List[node.Node] = n match {
    case null => result.reverse
    case n => getSiblings(n.getNext(), n :: result)
  }

}
