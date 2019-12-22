package gelatinous.site.template

import gelatinous.Index

object PostIndex extends Base with Index {
  import scalatags.Text.all._
  val route = "blog.html"
  val pageTitle = "Blog"
  def pageContent = PostCollection.articles.map(_.digest)
}