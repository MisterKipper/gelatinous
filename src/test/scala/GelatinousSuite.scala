package gelatinous

import java.nio.file._
import org.scalatest.flatspec.AnyFlatSpec
// import fs2._
// import cats._
// import cats.implicits._
import cats.effect._
import scala.concurrent.ExecutionContext
import scala.jdk.CollectionConverters._
import java.nio.file.attribute.BasicFileAttributes

class GelatinousSuite extends AnyFlatSpec {
  val source = Paths.get("src/test/resources/test-dir/")
  val target = Paths.get("target/test/test-dir/")
  val executionContext: ExecutionContext = ExecutionContext.global
  implicit val contextShiftIO: ContextShift[IO] = IO.contextShift(executionContext)

  def relativize(path: Path, source: Path, target: Path): Path = target.resolve(source.relativize(path))

  "Just playing" should "foo" in {
    assert {
      (for {
        blocker <- fs2.Stream.resource(Blocker[IO])
        foo <- fs2.Stream.eval(Util.copyDirectoryRecursively[IO](blocker, source, target))
      } yield foo).compile.lastOrError.unsafeRunSync()
      true
    }
  }
}
