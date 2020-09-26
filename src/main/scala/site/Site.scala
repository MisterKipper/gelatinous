package gelatinous.site

import gelatinous.GelatinousApp

object Site extends GelatinousApp {
  val assetDir = "assets"
  val sourceDir = "src/main/resources/"
  val targetDir = "target/site/"
  val standalonePages = List(Index, AboutMe)
  val collections = List(PostCollection)
}
