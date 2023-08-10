import java.nio.charset.StandardCharsets

import complete.DefaultParsers._

crossScalaVersions := Seq("3.3.0", "2.13.11", "2.12.18")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

scalacOptions         := Seq("-Ywarn-dead-code")
Test / scalacOptions  -= "-Ywarn-dead-code"

// Declares scalatest, scalacheck, minitest and utest dependencies explicitly.
libraryDependencies ++= Seq(
  "com.lihaoyi"       %% "utest"             % "0.8.1"    % Test,
  "org.scalatest"     %% "scalatest-funspec" % "3.2.16"   % Test,
  "org.scalatestplus" %% "scalacheck-1-17"   % "3.2.16.0" % Test,
  "org.scalacheck"    %% "scalacheck"        % "1.17.0"   % Test,
  "org.specs2"        %% "specs2-scalacheck" % "4.20.2"   % Test,
  "io.monix"          %% "minitest-laws"     % "2.9.6"    % Test,
  "org.scalameta"     %% "munit-scalacheck"  % "0.7.29"   % Test
)

doctestMarkdownEnabled    := true
doctestOnlyCodeBlocksMode := true
doctestMarkdownPathFinder := baseDirectory.value ** "*.md"

testFrameworks += new TestFramework("minitest.runner.Framework")

val existsInFile = inputKey[Unit]("Ensure a given string exists in a file")

existsInFile := {
  val Seq(searchKey, fileName) = spaceDelimited("<arg>").parsed
  val fileContent = IO.read(file(fileName), StandardCharsets.UTF_8)
  if (!fileContent.contains(searchKey)) {
    sys.error(s"$searchKey doesn't exist in $fileName")
  }
}
