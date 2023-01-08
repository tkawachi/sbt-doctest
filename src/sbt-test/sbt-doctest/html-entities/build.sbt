crossScalaVersions := Seq("2.13.10", "2.12.17")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

scalacOptions         := Seq("-Ywarn-dead-code")
Test / scalacOptions  -= "-Ywarn-dead-code"

// Declares scalatest, scalacheck, minitest and utest dependencies explicitly.
libraryDependencies ++= Seq(
  "com.lihaoyi"    %% "utest"             % "0.8.1" % Test,
  "org.scalatest"  %% "scalatest"         % "3.0.9"  % Test,
  "org.scalacheck" %% "scalacheck"        % "1.15.3" % Test,
  "io.monix"       %% "minitest-laws"     % "2.9.6"  % Test,
  "org.specs2"     %% "specs2-scalacheck" % "4.19.0" % Test,
  "org.scalameta"  %% "munit-scalacheck"  % "0.7.20" % Test
)

testFrameworks += new TestFramework("minitest.runner.Framework")

doctestDecodeHtmlEntities := true
