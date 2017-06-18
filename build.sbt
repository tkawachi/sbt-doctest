val scalatestVersion = "3.0.1"
val scalacheckVersion = "1.13.4"
val utestVersion = "0.4.7"

lazy val root = (project in file(".")).settings(
  buildInfoSettings: _*
).settings(
  sbtPlugin := true,
  organization := "com.github.tkawachi",
  name := "sbt-doctest",
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
  scmInfo := Some(ScmInfo(
    url("https://github.com/tkawachi/sbt-doctest/"),
    "scm:git:github.com:tkawachi/sbt-doctest.git"
  )),
  scalaVersion := "2.10.6",
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint"
  ),
  sourceGenerators in Compile += buildInfo.toTask,
  buildInfoPackage := "com.github.tkawachi.doctest",
  buildInfoObject := "DoctestBuildinfo",
  buildInfoKeys ++= Seq[BuildInfoKey](
    "utestVersion" -> utestVersion,
    "scalatestVersion" -> scalatestVersion,
    "scalacheckVersion" -> scalacheckVersion,
    "specs2Version" -> "3.8.7"
  ),
  libraryDependencies ++= Seq(
    "org.scala-lang"     %  "scala-compiler" % scalaVersion.value,
    "org.scalatest"      %% "scalatest"      % scalatestVersion % "test",
    "org.scalacheck"     %% "scalacheck"     % scalacheckVersion % "test",
    "commons-io"         %  "commons-io"     % "2.4",
    "org.apache.commons" %  "commons-lang3"  % "3.4"
  ),
  doctestTestFramework := DoctestTestFramework.ScalaTest,
  doctestMarkdownEnabled := true,
  doctestMarkdownPathFinder := (resourceDirectory in Test).value ** "*.md"
).settings(scalariformSettings: _*)
