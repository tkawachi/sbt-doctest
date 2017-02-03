scalaVersion := "2.11.7"

crossScalaVersions := "2.11.7" :: "2.12.1" :: Nil

scalacOptions += "-Xfatal-warnings"
scalacOptions += {
  CrossVersion.partialVersion(scalaVersion.value) match {
    case Some((2, n)) if n >= 11 =>
      "-Ywarn-unused-import"
    case _ =>
      ""
  }
}

doctestDecodeHtmlEntities := true
