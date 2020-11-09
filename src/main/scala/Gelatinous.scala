package gelatinous

import java.nio.file.{Files, Paths}
// import java.nio.charset.StandardCharsets
import scala.jdk.CollectionConverters._

// import Util.discard

class Gelatinous {
  @SuppressWarnings(Array("org.wartremover.warts.ToString"))
  def build(manifest: Manifest): Unit = {
    val source = Paths.get(manifest.sourceDir)
    val target = Paths.get(manifest.targetDir)

    Util.deleteDirectoryRecursively(target)
    Util.discard(Files.createDirectory(target))
    for (file <- Files.list(source).iterator().asScala) {
      val name = file.getParent().relativize(file).toString
      val handler = manifest.directoryHandlers.get(name)
      handler match {
        case None => println(s"No handler defined for directory $name")
        case Some(h) => h.writeContents(target)
      }
    }
    // TODO: build sitemap
  }
}
