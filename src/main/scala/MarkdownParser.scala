package gelatinous

import scala.annotation.tailrec
// import scala.collection.mutable.{Map => MutableMap}
import scala.jdk.CollectionConverters._

import org.commonmark.node
import org.commonmark.parser.Parser
import org.commonmark.ext.front.matter.YamlFrontMatterBlock
import org.commonmark.ext.front.matter.YamlFrontMatterExtension
import org.commonmark.ext.front.matter.YamlFrontMatterVisitor
// import Util.discard

// TODO: Rewrite to be properly functional and not have to ignore all these warts.
// @SuppressWarnings(
//   Array(
//     "org.wartremover.warts.Var",
//     "org.wartremover.warts.MutableDataStructures",
//     "org.wartremover.warts.DefaultArguments"
//   )
// )
@SuppressWarnings(Array("org.wartremover.warts.Null"))
object MarkdownParser {
  import scalatags.Text.all._
  private val parser: Parser = Parser
    .builder()
    .extensions(List(YamlFrontMatterExtension.create()).asJava)
    .build()

  def parse(markdown: Seq[String]): (ContentMetadata, Frag, Frag) = {
    val s = markdown.fold("")((acc, line) => acc ++ "\n" ++ line)
    val mdParsed = parser.parse(s)
    val postHtml = walkFullTree(mdParsed)
    val metadata = getMetadata(mdParsed.getFirstChild())
    val digest = walkTree(mdParsed, 3, 0)
    (metadata, postHtml, digest)
  }

  def getMetadata(n: node.Node): ContentMetadata = {
    val yamlVisitor = new YamlFrontMatterVisitor
    n.accept(yamlVisitor)
    ContentMetadata.fromMap(yamlVisitor.getData().asScala.map { case (k, v) => (k, v.get(0)) })
  }

  def walkFullTree(n: node.Node): Frag = {
    walkTree(n, Int.MaxValue, 0)
  }

  def walkTree(n: node.Node, maxDepth: Int, currentDepth: Int): Frag = {
    if (maxDepth - currentDepth === 0) {
      ""
    } else {
      convertNode(n, maxDepth, currentDepth + 1)
    }
  }

  def convertNode(n: node.Node, maxDepth: Integer, currentDepth: Integer): Frag = n match {
    case null => UnitFrag(())
    case _: YamlFrontMatterBlock => UnitFrag(())
    case n: node.Text => n.getLiteral()
    case _: node.SoftLineBreak => " "
    case n: node.Image => img(src := n.getDestination, title := n.getTitle)
    case n: Any => convertBranchNode(n, maxDepth, currentDepth)
  }

  def convertBranchNode(n: node.Node, maxDepth: Int, currentDepth: Int): Frag = {
    val f = n match {
      case _: node.Document => scalatags.Text.tags2.article(_)
      case n: node.Heading => convertHeading(n)
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
      case Some(c) => c.map(walkTree(_, maxDepth, currentDepth)).fold(frag())(frag(_, _))
      case None => UnitFrag(())
    }

    def g(xs: Modifier*): Frag = {
      f.apply(xs)
    }
    g(x)
  }

  def convertHeading(n: node.Heading): (Seq[Modifier] => Frag) = n.getLevel() match {
    case 1 => h1(_)
    case 2 => h2(_)
    case 3 => h3(_)
    case 4 => h4(_)
    case 5 => h5(_)
    case 6 => h6(_)
  }

  def getChildren(n: node.Node): Option[List[node.Node]] = n match {
    case null => None
    case n => {
      Some(getSiblings(n.getFirstChild, List()))
    }
  }

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
