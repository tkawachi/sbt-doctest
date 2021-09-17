import java.nio.charset.StandardCharsets

import complete.DefaultParsers._

crossScalaVersions := Seq("2.12.15", "2.13.6", "3.0.2")

// Declares scalatest, scalacheck dependencies explicitly.
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest-funspec" % "3.2.10" % Test,
  "org.scalatestplus" %% "scalacheck-1-15" % "3.2.10.0" % Test
)

doctestTestFramework := DoctestTestFramework.ScalaTest

val existsInFile = inputKey[Unit]("Ensure a given string exists in a file")

existsInFile := {
  val Seq(searchKey, fileName) = spaceDelimited("<arg>").parsed
  val fileContent = IO.read(file(fileName), StandardCharsets.UTF_8)
  if (!fileContent.contains(searchKey)) {
    sys.error(s"$searchKey doesn't exist in $fileName")
  }
}
