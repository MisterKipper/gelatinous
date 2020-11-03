package gelatinous

final case class ContentItem(metadata: ContentMetadata, content: String) {
  def render: String
}
