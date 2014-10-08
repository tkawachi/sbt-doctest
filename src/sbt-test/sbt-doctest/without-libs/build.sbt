// Declares scalatest and scalacheck dependencies explicitly.
libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.11.5" % "test"
)

crossScalaVersions := "2.11.2" :: "2.10.4" :: Nil

doctestWithDependencies := false

doctestSettings
