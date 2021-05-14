import java.nio.charset.StandardCharsets

import complete.DefaultParsers._

crossScalaVersions := Seq("2.13.5", "2.12.13")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

scalacOptions         := Seq("-Ywarn-dead-code")
scalacOptions in Test -= "-Ywarn-dead-code"
scalacOptions        ++= (scalaVersion.value match {
  case v if v startsWith "2.13." => Seq("-target:jvm-1.8")
  case v if v startsWith "2.12." => Seq("-target:jvm-1.8", "-opt:l:method")
})

// Declares scalatest, scalacheck, minitest and utest dependencies explicitly.
libraryDependencies ++= Seq(
  "com.lihaoyi"    %% "utest"             % "0.7.10"  % Test,
  "org.scalatest"  %% "scalatest"         % "3.2.9"  % Test,
  "org.scalacheck" %% "scalacheck"        % "1.15.3" % Test,
  "org.specs2"     %% "specs2-core"       % "4.11.0" % Test,
  "org.specs2"     %% "specs2-scalacheck" % "4.11.0" % Test,
  "io.monix"       %% "minitest"          % "2.9.5"  % Test,
  "io.monix"       %% "minitest-laws"     % "2.9.6"  % Test,
  "org.scalameta"  %% "munit-scalacheck"  % "0.7.20" % Test
)

doctestMarkdownEnabled    := true
doctestOnlyCodeBlocksMode := true
doctestMarkdownPathFinder := baseDirectory.value ** "*.md"

testFrameworks += new TestFramework("minitest.runner.Framework")
testFrameworks += new TestFramework("munit.Framework")

val existsInFile = inputKey[Unit]("Ensure a given string exists in a file")

existsInFile := {
  val Seq(searchKey, fileName) = spaceDelimited("<arg>").parsed
  val fileContent = IO.read(file(fileName), StandardCharsets.UTF_8)
  if (!fileContent.contains(searchKey)) {
    sys.error(s"$searchKey doesn't exist in $fileName")
  }
}
