package gelatinous

final case class Manifest(
    sourceDir: String,
    targetDir: String,
    assetDir: String,
    pages: Tree[Template],
)
