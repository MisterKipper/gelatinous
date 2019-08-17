package gelatinous.site

import template._

object Manifest extends gelatinous.Manifest {
  val sourceDir = "gelatinous-site/src/main/resources/"
  val targetDir = "target/site/"
  val standalonePages = List(AboutMe)
  val assetDir = "assets"
  val collections = List(PostCollection)
}
