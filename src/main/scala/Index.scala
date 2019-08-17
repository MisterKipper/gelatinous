package gelatinous

trait Index extends PrettyText {
  val route: String
  // val myHtml: scalatags.Text.TypedTag[String]
  def render: String
}
