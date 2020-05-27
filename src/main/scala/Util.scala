package gelatinous
import java.text.SimpleDateFormat

object Util {
  val dateFormatString = "yyyy-MM-dd"
  def convertStringToDate(s: String): java.util.Date = {
    val dateFormat = new SimpleDateFormat(dateFormatString)
    dateFormat.parse(s)
  }
  def Descending[T: Ordering]: scala.math.Ordering[T] = implicitly[Ordering[T]].reverse
}
