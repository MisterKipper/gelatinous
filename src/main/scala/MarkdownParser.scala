package gelatinous

import scala.collection.mutable.{Map => MutableMap}
import scala.jdk.CollectionConverters._

import org.commonmark.node._
import org.commonmark.parser.Parser
import org.commonmark.ext.front.matter.YamlFrontMatterBlock
import org.commonmark.ext.front.matter.YamlFrontMatterNode
import org.commonmark.ext.front.matter.YamlFrontMatterExtension
import scalatags.Text

class MarkdownParser extends AbstractParser {
  val parser = Parser
    .builder()
    .extensions(List(YamlFrontMatterExtension.create()).asJava)
    .build()
  def parse(markdown: String, nNodes: Int = 2) = {
    val mdParsed = parser.parse(markdown)
    val postHtml = MarkdownParser.walkTree(mdParsed)
    MarkdownParser.nNodes = 0
    val digest = MarkdownParser.walkTree(mdParsed, nNodes)
    val metadata = MarkdownParser.metadata.toMap
    (metadata, postHtml, digest)
  }
}

object MarkdownParser {
  import Text.all._
  private var nNodes = 0
  val metadata: MutableMap[String, String] = MutableMap()
  def walkTree(node: Node, maxNodes: Int = -1): Frag = {
    if (maxNodes - nNodes == 0) {
      println(node)
    } else {
      node match {
        case null => UnitFrag(())
        case node: Text => {
          nNodes += 1
          node.getLiteral()
        }
        case _: SoftLineBreak => " "
        case node: Image => img(src := node.getDestination, title := node.getTitle)
        case node: YamlFrontMatterNode => {
          metadata += (node.getKey() -> node.getValues().get(0))
          UnitFrag(())
        }
        case node: YamlFrontMatterBlock => {
          getChildren(node).get.map(walkTree(_, maxNodes))
        }
        case node: Any => {
          val f = node match {
            case _: Document => Text.tags2.article
            case node: Heading => {
              node.getLevel() match {
                case 1 => h1
                case 2 => h2
                case 3 => h3
                case 4 => h4
                case 5 => h5
                case 6 => h6
              }
            }
            case _: Paragraph => p
            case _: BulletList => ul
            case _: ListItem => li
            case _: Emphasis => em
            case _: BlockQuote => blockquote
            case _: Code => code
            case _: HardLineBreak => br
            case _: OrderedList => ol
            case _: StrongEmphasis => b
            case _: ThematicBreak => hr
            case node: Link => a(href := node.getDestination(), title := node.getTitle())
            case node: Any => {
              println(s"Unknown commonmark node type: $node")
              tag(s"${node.toString().dropRight(2)}")
            }
          }
          f(getChildren(node).get.map(walkTree(_, maxNodes - 1)).fold(frag())(frag(_, _)))
        }
      }
    }
  }

  def getChildren(node: Node): Option[List[Node]] = node match {
    case null => None
    case n => {
      Some(getSiblings(n.getFirstChild, List()))
    }
  }

  def getSiblings(node: Node, result: List[Node]): List[Node] = node match {
    case null => result.reverse
    case n => getSiblings(node.getNext(), n :: result)
  }
}
