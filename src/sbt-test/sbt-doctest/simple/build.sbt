scalaVersion := "2.10.6"

crossScalaVersions := "2.10.6" :: "2.11.7" :: Nil

scalacOptions += "-Xfatal-warnings"
scalacOptions += {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, n)) if n >= 11 =>
      "-Ywarn-unused-import"
    case _ =>
      ""
  }
}

doctestMarkdownEnabled := true
