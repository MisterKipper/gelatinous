package gelatinous
package site

object Site extends Gelatinous {
  val assetDir = "assets"
  val sourceDir = "src/main/resources/"
  val targetDir = "target/site/"
  val standalonePages = List(Index, AboutMe)
  val collections = List(PostCollection)
}
