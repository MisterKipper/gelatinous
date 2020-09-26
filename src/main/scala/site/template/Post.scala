package gelatinous.site

import scalatags.Text.all.Frag

import gelatinous.{Article, PrettyText}
import gelatinous.MarkdownParser
import gelatinous.site.PostCollection

class Post(data: List[String]) extends Article(data, new MarkdownParser) with Base with PrettyText {
  // val (metadata, postHtml, digest) = parse(data.reduce((result, line) => result + '\n' ++ line))
  val pageTitle: String = metadata("title")
  val timestamp: String = metadata("date")
  def pageContent(): Frag = postHtml
  val slug: String = pageTitle.toLowerCase.replace(' ', '-')
  val route: String = PostCollection.baseRoute + slug + ".html"
}
