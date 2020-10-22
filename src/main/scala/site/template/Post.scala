package gelatinous
package site

import scalatags.Text.all.Frag

class Post(dataIn: List[String]) extends Article with Base {
  // val (metadata, postHtml, digest) = parse(data.reduce((result, line) => result + '\n' ++ line))
  val data: List[String] = dataIn
  val pageTitle: String = metadata("title")
  val timestamp: String = metadata("date")
  def pageContent(): Frag = postHtml
  val slug: String = pageTitle.toLowerCase.replace(' ', '-')
  val route: String = PostCollection.baseRoute + slug + ".html"
}
