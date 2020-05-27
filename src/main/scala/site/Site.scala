package gelatinous.site

import gelatinous.Gelatinous

object Site {
  def main(args: Array[String]): Unit = {
    val gelatinous = new Gelatinous(Manifest)
    gelatinous.build()
  }
}
