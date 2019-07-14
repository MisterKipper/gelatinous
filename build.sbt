lazy val root = (project in file(".")).
  settings(
    inThisBuild(List(
                  scalaVersion := "2.13.0"
                )),
    name := "gelatinous",
    version := "0.1.0",
    mainClass in Compile := Some("Gelatinous")
  )

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % Test
