package gelatinous.site.util

import org.commonmark.node._
import scalatags.Text

object ScalatagsVisitor {
  import Text.all._
  var nNodes = 0
  def walkTree(node: Node, length: Option[Int] = None): Frag = {
    node match {
      case null => frag()
      case node: Text => node.getLiteral()
      case node: Image => img(src := node.getDestination, title := node.getTitle)
      case node: Any => {
        val f = node match {
          case _: Document => Text.tags2.article
          case node: Heading => {
            nNodes += 1
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
            nNodes += 1
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
          case node: Any => {
            println(s"Unknown commonmark node type: $node")
            tag("unknowntag")
          }
        }
        length match {
          case Some(length) =>
            if (nNodes >= length) {
              frag()
            } else {
              f(getChildren(node).get.map(walkTree(_, Some(length))).fold(frag())(frag(_, _)))
            }
          case None => f(getChildren(node).get.map(walkTree(_, None)).fold(frag())(frag(_, _)))
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
