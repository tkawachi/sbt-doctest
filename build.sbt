lazy val root = (project in file(".")).settings(
  crossSbtVersions := Vector("1.2.8"), // Don't update https://github.com/sbt/sbt/issues/5049
  organization := "com.github.tkawachi",
  name := "sbt-doctest",
  licenses := Seq("MIT" -> url("https://opensource.org/licenses/MIT")),
  scmInfo := Some(ScmInfo(
    url("https://github.com/tkawachi/sbt-doctest/"),
    "scm:git:github.com:tkawachi/sbt-doctest.git"
  )),
  javacOptions ++= Seq(
    "-encoding", "UTF-8"),
  scalacOptions ++= Seq(
    "-deprecation",
    "-encoding", "UTF-8",
    "-feature",
    "-unchecked",
    "-Xlint:-unused,_"
  ),
  libraryDependencies ++= Seq(
    "commons-io"         %  "commons-io"          % "2.9.0",
    "org.apache.commons" %  "commons-lang3"       % "3.12.0",
    "org.scalameta"      %% "scalameta"           % "4.4.18",
    "com.lihaoyi"        %% "utest"               % "0.7.10"     % Test,
    "org.scalatest"      %% "scalatest"           % "3.0.9"     % Test,
    "org.specs2"         %% "specs2-scalacheck"   % "4.12.0"    % Test,
    "io.monix"           %% "minitest-laws"       % "2.9.6"     % Test
  ),
  testFrameworks += new TestFramework("utest.runner.Framework"),
  testFrameworks += new TestFramework("munit.Framework")
).enablePlugins(SbtPlugin)
