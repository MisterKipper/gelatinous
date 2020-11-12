package gelatinous
package site

import java.nio.file.Paths

object Site extends App {
  val sourceDir = "src/main/resources/"
  val targetDir = "target/site/"
  val pages: List[Renderable] = List(Home, AboutMe)
  val directoryHandlers: Map[String, Handler] = Map("assets" -> new StaticHandler(Paths.get(sourceDir + "assets"))) //, "posts" -> new MarkdownHandler(Blog))
  val manifest: Manifest = Manifest(sourceDir, targetDir, pages, directoryHandlers)
  Gelatinous.build(manifest)
}
