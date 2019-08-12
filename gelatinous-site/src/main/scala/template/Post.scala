package gelatinous.site.template

import gelatinous.Text

class Post(markdown: String) extends Base {
  import Text.all._
  type Metadata = Map[String, String]
  val metadataDelimiter = "----"
  val (metadata, mainHtml) = parseMarkdown(markdown)
  val pageTitle = metadata("title")
  val url = "posts/"

  def parseMarkdown(markdown: String): (Metadata, Frag) = {
    val sections = markdown.split(metadataDelimiter)
    val metadata = parseMetadata(sections(1))
    val bodyHtml = toScalatags(sections.slice(2, sections.size).reduce(_ + _))
    (metadata, bodyHtml)
  }

  def parseMetadata(metadata: String): Metadata = {
    Map.from(metadata
               .linesIterator
               .map(line => line.split(":").map(_.trim))
               .map(a => a(0) -> a(1)))
  }

  override def pageContent = {
    mainHtml
  }

  def toScalatags(markdown: String): Frag = {
    // TODO: Proper parser (or use pandoc?)
    def stupidParser(lines: Iterator[String], tags: Frag): Frag = lines.next match {
      case l if l.startsWith("# ") => frag(tags, h1(l.slice(2, l.size)))
      case l => frag(tags, p(l))
    }
    stupidParser(markdown.linesIterator, frag())
  }

  def digest: Digest = {
    // How to manipulate a scalatags Frag??
    new Digest(this)
  }
}

object Post {
  def apply(markdown: String) = new Post(markdown)
}
