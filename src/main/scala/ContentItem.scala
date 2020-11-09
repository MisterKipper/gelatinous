package gelatinous

import java.nio.file.{Files, Path}

sealed trait ContentItem {
  val metadata: ContentMetadata
  val content: String
  def write(target: Path): Unit
}

final case class StaticItem(metadata: ContentMetadata, content: String) extends ContentItem {
  def write(target: Path): Unit = {
    Util.discard(Files.copy(metadata.source, target))
  }
}

final case class PostItem(metadata: ContentMetadata, content: String) extends ContentItem {
  def write(target: Path): Unit = {
    Util.discard(Files.writeString(target, content))
  }
}
