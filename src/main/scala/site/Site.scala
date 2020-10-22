package gelatinous
package site

object Site extends Gelatinous {
  val sourceDir = "src/main/resources/"
  val targetDir = "target/site/"
  val directoryHandlers = Map("assets" -> SimpleDirectoryHandler, "posts" -> SimpleDirectoryHandler)
}
