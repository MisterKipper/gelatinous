package gelatinous

import java.util.Date
import java.text.SimpleDateFormat

final case class ContentMetadata(date: Date, title: String) // TODO: escape title

object ContentMetadata {
  def fromMap(map: Map[String, String]) = {
    val format = new SimpleDateFormat("yyyy-MM-dd")
    new ContentMetadata(format.parse(map("date")), map("title"))
  }
}
