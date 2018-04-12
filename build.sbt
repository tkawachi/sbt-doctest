val versions = new {
  val ScalaTest  = "3.0.3"
  val ScalaCheck = "1.13.5"
  val Specs2     = "3.9.4"
  val utest      = "0.4.8"
  val Minitest   = "2.1.1"
  val CommonsIO  = "2.4"
  val Lang3      = "3.4"
}

lazy val root = (project in file(".")).settings(
  sbtPlugin := true,
  crossSbtVersions := Vector("0.13.16", "1.0.0"),
  organization := "com.github.tkawachi",
  name := "sbt-doctest",
  licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
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
    "-Xfatal-warnings"
  ) ++ (if (scalaVersion.value startsWith "2.10.")
          List("-Xlint", "-target:jvm-1.6")
        else
          List("-Xlint:-unused,_")),
  libraryDependencies ++= Seq(
    "org.scala-lang"     %  "scala-compiler"      % scalaVersion.value,
    "commons-io"         %  "commons-io"          % versions.CommonsIO,
    "org.apache.commons" %  "commons-lang3"       % versions.Lang3,
    "com.lihaoyi"        %% "utest"               % versions.utest        % Provided,
    "org.scalatest"      %% "scalatest"           % versions.ScalaTest    % Provided,
    "org.scalacheck"     %% "scalacheck"          % versions.ScalaCheck   % Provided,
    "org.specs2"         %% "specs2-core"         % versions.Specs2       % Provided,
    "org.specs2"         %% "specs2-scalacheck"   % versions.Specs2       % Provided,
    "io.monix"           %% "minitest"            % versions.Minitest     % Provided,
    "io.monix"           %% "minitest-laws"       % versions.Minitest     % Provided
  ),

  // allows this plugin to eat its own dog food
  inConfig(Compile)(
    Seq(
      libraryDependencies ++= Seq(
        "com.lihaoyi"        %% "utest"               % versions.utest        % Test,
        "org.scalatest"      %% "scalatest"           % versions.ScalaTest    % Test,
        "org.scalacheck"     %% "scalacheck"          % versions.ScalaCheck   % Test,
        "org.specs2"         %% "specs2-core"         % versions.Specs2       % Test,
        "org.specs2"         %% "specs2-scalacheck"   % versions.Specs2       % Test,
        "io.monix"           %% "minitest"            % versions.Minitest     % Test,
        "io.monix"           %% "minitest-laws"       % versions.Minitest     % Test
      )
    )
  ),
  testFrameworks += new TestFramework("utest.runner.Framework")
).settings(scalariformSettings: _*)
