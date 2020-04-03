package gelatinous

import scala.collection.mutable.{Map => MutableMap}
import scala.jdk.CollectionConverters._

import org.commonmark.node
import org.commonmark.parser.Parser
import org.commonmark.ext.front.matter.YamlFrontMatterBlock
import org.commonmark.ext.front.matter.YamlFrontMatterNode
import org.commonmark.ext.front.matter.YamlFrontMatterExtension

class MarkdownParser extends AbstractParser {
  val parser = Parser
    .builder()
    .extensions(List(YamlFrontMatterExtension.create()).asJava)
    .build()
  def parse(markdown: String, nNodes: Int = 3) = {
    val mdParsed = parser.parse(markdown)
    val postHtml = MarkdownParser.walkTree(mdParsed)
    val metadata = MarkdownParser.metadata.toMap
    MarkdownParser.nNodes = 0
    val digest = MarkdownParser.walkTree(mdParsed, nNodes)
    (metadata, postHtml, digest)
  }
}

object MarkdownParser {
  import scalatags.Text.all._
  private var nNodes = 0
  val metadata: MutableMap[String, String] = MutableMap()
  def walkTree(n: node.Node, maxNodes: Int = -1): Frag = {
    if (maxNodes - nNodes == 0) {
      ""
    } else {
      n match {
        case null => UnitFrag(())
        case n: node.Text => {
          nNodes += 1
          n.getLiteral()
        }
        case _: node.SoftLineBreak => " "
        case n: node.Image => img(src := n.getDestination, title := n.getTitle)
        case n: YamlFrontMatterNode => {
          metadata += (n.getKey() -> n.getValues().get(0))
          UnitFrag(())
        }
        case n: YamlFrontMatterBlock => {
          getChildren(n).get.map(walkTree(_, maxNodes))
        }
        case n: Any => {
          val f = n match {
            case _: node.Document => scalatags.Text.tags2.article
            case n: node.Heading => {
              val i = if (maxNodes > 0) 1 else 0
              n.getLevel() + i match {
                case 1 => h1
                case 2 => h2
                case 3 => h3
                case 4 => h4
                case 5 => h5
                case 6 => h6
              }
            }
            case _: node.Paragraph => p
            case _: node.BulletList => ul
            case _: node.ListItem => li
            case _: node.Emphasis => em
            case _: node.BlockQuote => blockquote
            case _: node.Code => code
            case _: node.HardLineBreak => br
            case _: node.OrderedList => ol
            case _: node.StrongEmphasis => b
            case _: node.ThematicBreak => hr
            case n: node.Link => a(href := n.getDestination(), title := n.getTitle())
            case n: Any => {
              println(s"Unknown commonmark node type: $n")
              tag(s"${n.toString().dropRight(2)}")
            }
          }
          f(getChildren(n).get.map(walkTree(_, maxNodes)).fold(frag())(frag(_, _)))
        }
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
