sbtPlugin := true

name := "sbt-doctest"

organization := "com.github.tkawachi"

licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT"))

scmInfo := Some(ScmInfo(
  url("https://github.com/tkawachi/sbt-doctest/"),
  "scm:git:github.com:tkawachi/sbt-doctest.git"
))

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.scalatest" %% "scalatest" % "2.2.0" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.4" % "test",
  "commons-io" % "commons-io" % "2.4"
)

scalariformSettings

//DoctestPlugin.doctestSettings
