crossScalaVersions := Seq("2.10.7", "2.11.12", "2.12.12")

javacOptions ++= (scalaVersion.value match {
  case v if v startsWith "2.13." => Seq("-source", "1.8", "-target", "1.8")
  case v if v startsWith "2.12." => Seq("-source", "1.8", "-target", "1.8")
  case v if v startsWith "2.11." => Seq("-source", "1.8", "-target", "1.6")
  case v if v startsWith "2.10." => Seq("-source", "1.8", "-target", "1.6")
})

scalacOptions         := Seq("-Ywarn-dead-code")
scalacOptions in Test -= "-Ywarn-dead-code"
scalacOptions        ++= (scalaVersion.value match {
  case v if v startsWith "2.13." => Seq("-target:jvm-1.8")
  case v if v startsWith "2.12." => Seq("-target:jvm-1.8", "-opt:l:method")
  case v if v startsWith "2.11." => Seq("-target:jvm-1.6")
  case v if v startsWith "2.10." => Seq("-target:jvm-1.6")
})

// Declares scalatest, scalacheck, minitest and utest dependencies explicitly.
libraryDependencies ++= Seq(
  "com.lihaoyi"    %% "utest"             % "0.6.4"  % Test,
  "org.scalatest"  %% "scalatest"         % "3.2.3"  % Test,
  "org.scalacheck" %% "scalacheck"        % "1.14.0" % Test,
  "io.monix"       %% "minitest"          % "2.1.1"  % Test,
  "io.monix"       %% "minitest-laws"     % "2.1.1"  % Test
) ++ (
  if (scalaVersion.value.startsWith("2.10"))
    Seq(
      "org.specs2"     %% "specs2-core"       % "3.10.0"  % Test,
      "org.specs2"     %% "specs2-scalacheck" % "3.10.0"  % Test
    )
  else
    Seq(
      "org.specs2"     %% "specs2-core"       % "4.3.2"  % Test,
      "org.specs2"     %% "specs2-scalacheck" % "4.3.2"  % Test,
    )
  )

testFrameworks += new TestFramework("minitest.runner.Framework")

doctestDecodeHtmlEntities := true
