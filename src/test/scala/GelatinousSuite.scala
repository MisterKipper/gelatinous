package gelatinous

import java.nio.file._
import org.scalatest.flatspec.AnyFlatSpec
import scala.concurrent.ExecutionContext
import scala.jdk.CollectionConverters._
import java.nio.file.attribute.BasicFileAttributes

class GelatinousSuite extends AnyFlatSpec {
  val source: Path = Paths.get("src/test/resources/test-dir/")
  val target: Path = Paths.get("target/test/test-dir/")

  def relativize(path: Path, source: Path, target: Path): Path = target.resolve(source.relativize(path))

  "Just playing" should "foo" in {
    assert {
      true
    }
  }
}
