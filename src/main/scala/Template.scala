package gelatinous

// import scalatags.Text.{Frag, TypedTag}
import scalatags.Text.all.Frag

trait Template extends PrettyText {
  val myHtml: Frag
  val route: String
  def pageStyles(): Frag
  def pageContent(): Frag
  def pageScripts(): Frag
  def urlFor(folder: String, filename: String): String = {
    s"$folder/$filename"
  }
  def render: String = myHtml.pretty
}
