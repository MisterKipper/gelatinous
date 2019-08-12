package gelatinous.site

import gelatinous.Gelatinous

object Site extends App {
  val sourceDir = "gelatinous-site/src/main/resources"
  val targetDir = "target/site"

  val gelatinous = new Gelatinous(sourceDir, targetDir, Manifest)
  gelatinous.build()
}
