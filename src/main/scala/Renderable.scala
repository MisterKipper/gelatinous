package gelatinous

trait Renderable {
  val route: String
  def render(): String
}
