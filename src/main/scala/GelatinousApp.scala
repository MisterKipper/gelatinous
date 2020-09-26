package gelatinous

import cats.effect._

trait GelatinousApp extends IOApp {
  val assetDir: String
  val sourceDir: String
  val targetDir: String
  val standalonePages: List[Template]
  val collections: List[ArticleCollection[Article]]

  lazy val manifest: Manifest = Manifest(sourceDir, targetDir, assetDir, standalonePages, collections)

  def run(args: List[String]): IO[ExitCode] = {
    for {
      blocker <- fs2.Stream.resource(Blocker[IO])
      foo <- Gelatinous.build[IO](blocker, manifest)
    } yield foo //.compile.lastOrError.unsafeRunSync()
  }
}
