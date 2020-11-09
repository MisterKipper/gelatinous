package gelatinous

import java.nio.file.Path

trait Handler {
  def writeContents(target: Path): Unit
}
