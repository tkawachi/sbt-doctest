val versions = new {
  val ScalaTest  = "3.0.1"
  val ScalaCheck = "1.13.4"
  val Specs2     = "3.8.7"
  val utest      = "0.4.8"
  val CommonsIO  = "2.4"
  val Lang3      = "3.4"
}

lazy val root = (project in file(".")).settings(
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
  libraryDependencies ++= Seq(
    "org.scala-lang"     %  "scala-compiler"      % scalaVersion.value,
    "commons-io"         %  "commons-io"          % versions.CommonsIO,
    "org.apache.commons" %  "commons-lang3"       % versions.Lang3,
    "com.lihaoyi"        %% "utest"               % versions.utest        % "provided",
    "org.scalatest"      %% "scalatest"           % versions.ScalaTest    % "provided",
    "org.scalacheck"     %% "scalacheck"          % versions.ScalaCheck   % "provided",
    "org.specs2"         %% "specs2-core"         % versions.Specs2       % "provided",
    "org.specs2"         %% "specs2-scalacheck"   % versions.Specs2       % "provided"
  ),

  // allows this plugin to eat its own dog food
  inConfig(Compile)(
    Seq(
      libraryDependencies ++= Seq(
        "com.lihaoyi"        %% "utest"               % versions.utest        % "test",
        "org.scalatest"      %% "scalatest"           % versions.ScalaTest    % "test",
        "org.scalacheck"     %% "scalacheck"          % versions.ScalaCheck   % "test",
        "org.specs2"         %% "specs2-core"         % versions.Specs2       % "test",
        "org.specs2"         %% "specs2-scalacheck"   % versions.Specs2       % "test"
      )
    )
  ),
  testFrameworks += new TestFramework("utest.runner.Framework")
).settings(scalariformSettings: _*)
