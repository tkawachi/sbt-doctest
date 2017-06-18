val scalatestVersion = "3.0.1"
val scalacheckVersion = "1.13.4"
val specs2Version = "3.8.7"
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
  javacOptions ++= Seq(
    "-source", "1.6",
    "-target", "1.6",
    "-encoding", "UTF-8"),
  scalaVersion := "2.10.6",
  scalacOptions ++= Seq(
    "-target:jvm-1.6",
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
    "specs2Version" -> specs2Version
  ),
  libraryDependencies ++= Seq(
    "org.scala-lang"     %  "scala-compiler"      % scalaVersion.value,
    "com.lihaoyi"        %% "utest"               % utestVersion        % "test",
    "org.scalatest"      %% "scalatest"           % scalatestVersion    % "provided",
    "org.scalacheck"     %% "scalacheck"          % scalacheckVersion   % "provided",
    "org.specs2"         %% "specs2-core"         % specs2Version       % "provided",
    "org.specs2"         %% "specs2-scalacheck"   % specs2Version       % "provided"
  ),
  doctestTestFramework := DoctestTestFramework.utest,
  doctestWithDependencies := false,
  doctestMarkdownEnabled := true,
  doctestMarkdownPathFinder := (resourceDirectory in Test).value ** "*.md",
  testFrameworks       := Seq(new TestFramework("utest.runner.Framework"))
).settings(scalariformSettings: _*)
