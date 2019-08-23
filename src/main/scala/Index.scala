package gelatinous

trait Index extends PrettyText {
  val route: String
  def render: String
}
