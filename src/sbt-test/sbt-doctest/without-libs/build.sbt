// Declares scalatest and scalacheck dependencies explicitly.
libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
)

crossScalaVersions := "2.11.7" :: "2.12.1" :: Nil

doctestWithDependencies := false
