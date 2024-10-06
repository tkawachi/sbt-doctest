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
    "commons-io"         %  "commons-io"          % "2.17.0",
    "org.apache.commons" %  "commons-lang3"       % "3.17.0",
    "org.scalameta"      %% "scalameta"           % "4.10.1" cross CrossVersion.for3Use2_13,
    "com.lihaoyi"        %% "utest"               % "0.8.4"     % Test,
    "org.scalatest"      %% "scalatest-funspec"   % "3.2.19"    % Test,
    "org.scalatestplus"  %% "scalacheck-1-18"     % "3.2.19.0"  % Test,
    "org.specs2"         %% "specs2-scalacheck"   % "4.20.8"    % Test,
    "io.monix"           %% "minitest-laws"       % "2.9.6"     % Test
  ),
  testFrameworks += new TestFramework("utest.runner.Framework"),
).enablePlugins(SbtPlugin)
