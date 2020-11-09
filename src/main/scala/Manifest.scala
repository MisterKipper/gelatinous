package gelatinous

final case class Manifest(
    sourceDir: String,
    targetDir: String,
    pages: List[Renderable],
    directoryHandlers: Map[String, Handler]
)
