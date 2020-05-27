package gelatinous.site

object Manifest extends gelatinous.Manifest {
  val sourceDir = "src/main/resources/"
  val targetDir = "target/site/"
  val assetDir = "assets"
  val standalonePages = List(Index, AboutMe)
  val collections = List(PostCollection, DemoCollection)
}
