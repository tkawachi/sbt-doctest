// Declares scalatest and scalacheck dependencies explicitly.
libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.11.5" % "test"
)

crossScalaVersions := "2.11.7" :: "2.10.6" :: Nil

doctestWithDependencies := false

doctestSettings
