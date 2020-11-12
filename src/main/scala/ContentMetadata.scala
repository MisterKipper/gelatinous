package gelatinous

import java.util.Date
import java.nio.file.{Path, Paths}

final case class ContentMetadata(date: Date, title: String, source: Path) // TODO: escape title

object ContentMetadata {
  def fromMap(map: collection.Map[String, String]): ContentMetadata = {
    new ContentMetadata(Util.convertStringToDate(map("date")), map("title"), Paths.get(map("source")))
  }
}
