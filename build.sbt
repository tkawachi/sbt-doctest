lazy val root = (project in file(".")).settings(
  sbtPlugin := true,
  organization := "com.github.tkawachi",
  name := "sbt-doctest",
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
  scmInfo := Some(ScmInfo(
    url("https://github.com/tkawachi/sbt-doctest/"),
    "scm:git:github.com:tkawachi/sbt-doctest.git"
  )),
  scalaVersion := "2.10.4",
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-Xfatal-warnings",
    "-Xlint"
  ),
  libraryDependencies ++= Seq(
    "org.scala-lang" %  "scala-compiler" % scalaVersion.value,
    "org.scalatest"  %% "scalatest"      % "2.2.3"  % "test",
    "org.scalacheck" %% "scalacheck"     % "1.12.1" % "test",
    "commons-io"     %  "commons-io"     % "2.4"
  ),
  doctestTestFramework := DoctestTestFramework.ScalaTest
).settings(scalariformSettings: _*)
 .settings(doctestSettings: _*)
