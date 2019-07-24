package gelatinous

import scalatags.Text.all._

class Template extends BaseTemplate {
  val pageTitle = "Home"

  def pageContent() = {
    frag(h2("My first scalatags page"), p("Thanks for coming."))
  }

  override def pageScripts() = {
    frag(script("What?"), super.pageScripts())
  }
}
