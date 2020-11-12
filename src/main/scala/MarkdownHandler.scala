package gelatinous

import java.nio.file.{FileVisitResult, Files, Path, SimpleFileVisitor}
import java.nio.file.attribute.BasicFileAttributes
import scala.jdk.CollectionConverters._

@SuppressWarnings(Array("org.wartremover.warts.All"))
class MarkdownHandler[A <: Index, B <: Template](source: Path, index: A, template: B) extends Handler {
  def buildContents(): (Renderable, List[ContentItem]) = {
    val items = scala.collection.mutable.ListBuffer.empty[ContentItem]
    Files.walkFileTree(
      source,
      new SimpleFileVisitor[Path] {

        override def visitFile(path: Path, attrs: BasicFileAttributes): FileVisitResult = {
          val lines = Files.readAllLines(path).asScala.toList
          val (metadata, content, _) = MarkdownParser.parse(lines)
          template.build(metadata, content)
          items.append(PostItem(metadata, content.render))
          FileVisitResult.CONTINUE
        }

      }
    )
    // val indexMetadata = ContentMetadata(java.util.Date.from(java.time.Instant.now()), "Index", source)
    val idx = index.build(items.toList)
    (idx, items.toList)
  }

  def writeContents(target: Path): Unit = {
    val (index, items) = buildContents()
    val root = target.resolve(source.getFileName())
    Files.createDirectory(root)
    Files.writeString(root.resolve("index.html"), index.render)
    for (item <- items) {
      val dir = root.resolve(item.metadata.title)
      Files.createDirectory(dir)
      Files.writeString(dir.resolve("index.html"), item.content)
    }

}

  def writeItem(target: Path, item: ContentItem): Unit = {
    Util.discard(Files.writeString(target, item.content))
  }
}
