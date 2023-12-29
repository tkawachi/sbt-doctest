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
    "commons-io"         %  "commons-io"          % "2.15.1",
    "org.apache.commons" %  "commons-lang3"       % "3.14.0",
    "org.scalameta"      %% "scalameta"           % "4.8.15",
    "com.lihaoyi"        %% "utest"               % "0.8.2"     % Test,
    "org.scalatest"      %% "scalatest-funspec"   % "3.2.17"    % Test,
    "org.scalatestplus"  %% "scalacheck-1-17"     % "3.2.17.0"  % Test,
    "org.specs2"         %% "specs2-scalacheck"   % "4.20.3"    % Test,
    "io.monix"           %% "minitest-laws"       % "2.9.6"     % Test
  ),
  testFrameworks += new TestFramework("utest.runner.Framework"),
).enablePlugins(SbtPlugin)
