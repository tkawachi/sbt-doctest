doctestSettings

scalaVersion := "2.10.4"

crossScalaVersions := "2.10.4" :: "2.11.2" :: Nil

scalacOptions += "-Xfatal-warnings"
scalacOptions += {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, n)) if n >= 11 =>
      "-Ywarn-unused-import"
    case _ =>
      ""
  }
}
