package gelatinous.site

import gelatinous.Gelatinous

object Site {
  def main(args: Array[String]) = {
    val gelatinous = new Gelatinous(Manifest)
    gelatinous.build()
  }
}
