package gelatinous.site.template

import gelatinous.{Article, PrettyText}
import gelatinous.MarkdownParser

class Post(data: List[String]) extends Article(data, new MarkdownParser) with Base with PrettyText {
  val (metadata, postHtml, digest) = parse(data.reduce((result, line) => result + '\n' ++ line))
  val pageTitle = metadata("title")
  val timestamp = metadata("date")
  def pageContent = postHtml
  val slug = pageTitle.toLowerCase.replace(' ', '-')
  val route = PostCollection.baseRoute + slug + ".html"
}
