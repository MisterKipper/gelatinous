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
  def parse(markdown: String) = {
    val parser = Parser
      .builder()
      .extensions(List(YamlFrontMatterExtension.create()).asJava)
      .build()
    val mdParsed = parser.parse(markdown)
    val postHtml = ScalatagsVisitor.walkTree(mdParsed)
    val metadata = ScalatagsVisitor.metadata.toMap
    (metadata, postHtml)
  }
}

object ScalatagsVisitor {
  import Text.all._
  var metadata: MutableMap[String, String] = MutableMap()
  // var nNodes = 0
  def walkTree(node: Node): Frag = {  //, length: Option[Int] = None): Frag = {
    node match {
      case null => UnitFrag(())
      case node: Text => node.getLiteral()
      case node: Image => img(src := node.getDestination, title := node.getTitle)
      case node: YamlFrontMatterNode => {
        metadata += (node.getKey() -> node.getValues().get(0))
        UnitFrag(())
      }
      case node: Any => {
        val f = node match {
          case _: Document => Text.tags2.article
          case node: Heading => {
            // nNodes += 1
            node.getLevel() match {
              case 1 => h1
              case 2 => h2
              case 3 => h3
              case 4 => h4
              case 5 => h5
              case 6 => h6
            }
          }
          case _: Paragraph => {
            // nNodes += 1
            p
          }
          case _: BulletList => ul
          case _: ListItem => li
          case _: Emphasis => em
          case _: BlockQuote => blockquote
          case _: Code => code
          case _: HardLineBreak => br
          case _: OrderedList => ol
          case _: StrongEmphasis => b
          case _: ThematicBreak => hr
            // case _: SoftLineBreak => "\n"
          case node: Link => a(href := node.getDestination(), title := node.getTitle())
          case _: YamlFrontMatterBlock => tag("yaml-front-matter-block", true)
          case node: Any => {
            println(s"Unknown commonmark node type: $node")
            tag(s"${node.toString().dropRight(2)}")
          }
        }
        f(getChildren(node).get.map(walkTree).fold(frag())(frag(_, _)))
        // length match {
        //   case Some(length) =>
        //     if (nNodes >= length) {
        //       frag()
        //     } else {
        //       f(getChildren(node).get.map(walkTree(_, Some(length))).fold(frag())(frag(_, _)))
        //     }
        //   case None => f(getChildren(node).get.map(walkTree(_, None)).fold(frag())(frag(_, _)))
        // }
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
