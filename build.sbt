sbtPlugin := true

name := "sbt-doctest"

organization := "com.github.tkawachi"

scalaVersion := "2.10.4"

libraryDependencies ++= Seq(
  "org.scala-lang" % "scala-compiler" % scalaVersion.value,
  "org.scalatest" %% "scalatest" % "2.2.0" % "test",
  "commons-io" % "commons-io" % "2.4"
)

scalariformSettings

DoctestPlugin.doctestSettings