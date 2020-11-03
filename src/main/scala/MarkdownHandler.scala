package gelatinous

import java.nio.file.{Files, FileVisitResult, Path, Paths, SimpleFileVisitor}
import cats.effect._

@SuppressWarnings(
  Array("org.wartremover.warts.Any", "org.wartremover.warts.Nothing", "org.wartremover.warts.NonUnitStatements")
)
class MarkdownHandler(indexTemplate: IndexTemplate, itemTemplate: ItemTemplate) extends Handler {
  def apply[F[_]: Sync: ContextShift](blocker: Blocker, source: Path, target: Path): F[Unit] = {
    blocker.delay {
      Files.walkFileTree(
        source,
        new SimpleFileVisitor[Path] {
          val root = target.resolve(source.getFileName())
          Files.createDirectory(root)
          Files.writeString(root.resolve("index.html"), indexTemplate.render)

          override def visitFile(path: Path): FileVisitResult = {
            val lines = Files.readAllLines(path)
            val item = itemTemplate.create(lines)
            val p = root.resolve(Paths.get(path.getFileName().toString().split("\\.", 2)(0)))
            Files.createDirectory(p)
            Files.writeString(p.resolve("index.html"), item.myHtml.render)
            FileVisitResult.CONTINUE
          }
        }
      )
    }
  }
}

// object MarkdownHandler {
//   def apply(index: Template, item: Template): MarkdownHandler = new MarkdownHandler(index, item)
// }
