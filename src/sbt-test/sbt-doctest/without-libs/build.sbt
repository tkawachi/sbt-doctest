// Declares scalatest and scalacheck dependencies explicitly.
libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
)

scalaVersion := "2.10.6"

crossScalaVersions := /* "2.10.6" :: / "2.11.11" :: "2.12.2" :: Nil

scalacOptions         := Seq("-Ywarn-dead-code")
scalacOptions in Test -= "-Ywarn-dead-code"
scalacOptions        ++= (scalaVersion.value match {
  case v if v startsWith "2.13." => "-target:jvm-1.8" :: Nil
  case v if v startsWith "2.12." => "-target:jvm-1.8" :: "-opt:l:method" :: Nil
  case v if v startsWith "2.11." => "-target:jvm-1.6" :: Nil
  case v if v startsWith "2.10." => "-target:jvm-1.6" :: Nil
})

doctestWithDependencies := false
