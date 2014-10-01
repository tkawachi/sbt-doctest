// Declares scalatest and scalacheck dependencies explicitly.
libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "2.2.2" % "test",
  "org.scalacheck" %% "scalacheck" % "1.11.6" % "test"
)

crossScalaVersions := "2.11.2" :: "2.10.4" :: Nil

doctestSettings

doctestWithDependencies := false
