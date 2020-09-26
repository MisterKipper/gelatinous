package gelatinous

final case class Manifest(
    sourceDir: String,
    targetDir: String,
    assetDir: String,
    standalonePages: List[Template],
    collections: List[ArticleCollection[Article]]
)
