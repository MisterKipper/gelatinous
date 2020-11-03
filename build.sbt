lazy val commonSettings = Seq(
  scalaVersion := "2.13.2",
  organization := "es.kyledavi",
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding",
    "utf-8",
    "-explaintypes",
    "-feature",
    // "-language:existentials",
    "-language:experimental.macros",
    "-language:higherKinds",
    // "-language:implicitConversions",
    "-unchecked",
    "-Xcheckinit",
    // "-Xfatal-warnings",
    "-Xlint:adapted-args",
    "-Xlint:constant",
    "-Xlint:delayedinit-select",
    "-Xlint:doc-detached",
    "-Xlint:inaccessible",
    "-Xlint:infer-any",
    "-Xlint:missing-interpolator",
    "-Xlint:nullary-override",
    "-Xlint:nullary-unit",
    "-Xlint:option-implicit",
    "-Xlint:package-object-classes",
    "-Xlint:poly-implicit-overload",
    "-Xlint:private-shadow",
    "-Xlint:stars-align",
    "-Xlint:type-parameter-shadow",
    // "-Ybackend-parallelism", "2",
    "-Ycache-plugin-class-loader:last-modified",
    "-Ycache-macro-class-loader:last-modified",
    // "-Ypartial-unification",
    "-Ywarn-dead-code",
    "-Ywarn-extra-implicit",
    "-Ywarn-numeric-widen",
    // "-Ywarn-unused",
    "-Ywarn-value-discard"
    // "-Yno-predef",  // no automatic import of Predef (removes irritating implicits)
    // "-Yno-imports"  // no automatic imports at all; all symbols must be imported explicitly
    // "-P:silencer:checkUnused",
  ),
  (scalacOptions in (Compile, compile)) ++= Seq("-Ywarn-unused", "-Xfatal-warnings"),
  // (scalacOptions in (Test, run)) --= Seq("-Ywarn-unused:imports", "-Xfatal-warnings"),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.2.0" % "test" withSources () withJavadoc (),
    "org.typelevel" %% "cats-core" % "2.1.1" withSources () withJavadoc (),
    "org.typelevel" %% "cats-effect" % "2.1.4" withSources () withJavadoc (),
    "co.fs2" %% "fs2-io" % "2.4.4" withSources () withJavadoc (),
    "com.lihaoyi" %% "scalatags" % "0.9.1" withSources () withJavadoc (),
    // "com.lihaoyi" % "ammonite" % "2.1.4" % "test" cross CrossVersion.full,
    "com.atlassian.commonmark" % "commonmark" % "0.15.2" withSources () withJavadoc (),
    "com.atlassian.commonmark" % "commonmark-ext-yaml-front-matter" % "0.15.2" withSources () withJavadoc ()
  ),
  wartremoverErrors ++= Warts.all

  // sourceGenerators in Test += Def.task {
  //   val file = (sourceManaged in Test).value / "amm.scala"
  //   IO.write(file, """object amm extends App { ammonite.Main.main(args) }""")
  //   Seq(file)
  // }.taskValue
  // // wartremoverWarnings ++= Warts.all
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "gelatinous",
    version := "0.1.0",
    mainClass in (Compile, run) := Some("gelatinous.site.Site")
  )

(fullClasspath in Test) ++= {
  (updateClassifiers in Test).value.configurations
    .find(_.configuration.name == Test.name)
    .get
    .modules
    .flatMap(_.artifacts)
    .collect { case (a, f) if a.classifier == Some("sources") => f }
}
