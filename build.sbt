lazy val commonSettings = Seq(
  scalaVersion := "2.13.1",
  organization := "es.kyledavi",
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "utf-8",
    "-explaintypes",
    "-feature",
    // "-language:existentials",
    "-language:experimental.macros",
    "-language:higherKinds",
    // "-language:implicitConversions",
    "-unchecked",
    "-Xcheckinit",
    "-Xfatal-warnings",
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
    "-Ywarn-unused",
    "-Ywarn-value-discard",
    // "-Yno-predef",  // no automatic import of Predef (removes irritating implicits)
    // "-Yno-imports"  // no automatic imports at all; all symbols must be imported explicitly
    // "-P:silencer:checkUnused",
    ),
  (scalacOptions in (Compile, console)) --= Seq("-Ywarn-unused", "-Xfatal-warnings"),
  // (scalacOptions in (Test, run)) --= Seq("-Ywarn-unused:imports", "-Xfatal-warnings"),
  libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "3.0.8" % Test,
    "org.typelevel" %% "cats-core" % "2.0.0",
    "org.typelevel" %% "cats-effect" % "2.1.3" withSources() withJavadoc(),
    "com.lihaoyi" %% "scalatags" % "0.9.1",
    "com.atlassian.commonmark" % "commonmark" % "0.15.0",
    "com.atlassian.commonmark" % "commonmark-ext-yaml-front-matter" % "0.15.0",
    ),
    wartremoverErrors ++= Warts.all
    // wartremoverWarnings ++= Warts.all
)

lazy val root = (project in file("."))
  .settings(
    commonSettings,
    name := "gelatinous",
    version := "0.1.0",
    mainClass in (Compile, run) := Some("gelatinous.site.Site")
  )
