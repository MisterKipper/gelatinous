package gelatinous
import java.text.SimpleDateFormat

object Util {
  val dateFormatString = "yyyy-MM-dd"
  def convertStringToDate(s: String) = {
    val dateFormat = new SimpleDateFormat(dateFormatString)
    dateFormat.parse(s)
  }
}
