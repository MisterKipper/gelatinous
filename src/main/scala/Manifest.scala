package gelatinous

import java.nio.file.Paths

trait Manifest {
  val sourceDir: String
  val targetDir: String
  val standalonePages: List[Template]
  val collections: List[ArticleCollection]
  val assetDir: String
  lazy val assetPath = Paths.get(assetDir)
}
