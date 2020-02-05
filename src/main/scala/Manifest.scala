package gelatinous

trait Manifest {
  val sourceDir: String
  val targetDir: String
  val assetDir: String
  val standalonePages: List[Template]
  val collections: List[ArticleCollection[Article]]
}
