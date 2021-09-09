crossScalaVersions := Seq("2.13.5", "2.12.14")

javacOptions ++= Seq("-source", "1.8", "-target", "1.8")

scalacOptions         := Seq("-Ywarn-dead-code")
scalacOptions in Test -= "-Ywarn-dead-code"
scalacOptions        ++= (scalaVersion.value match {
  case v if v startsWith "2.13." => Seq("-target:jvm-1.8")
  case v if v startsWith "2.12." => Seq("-target:jvm-1.8", "-opt:l:method")
})

// Declares scalatest, scalacheck, minitest and utest dependencies explicitly.
libraryDependencies ++= Seq(
  "com.lihaoyi"    %% "utest"             % "0.7.10" % Test,
  "org.scalatest"  %% "scalatest"         % "3.0.9"  % Test,
  "org.scalacheck" %% "scalacheck"        % "1.15.3" % Test,
  "io.monix"       %% "minitest-laws"     % "2.9.6"  % Test,
  "org.specs2"     %% "specs2-scalacheck" % "4.12.12" % Test,
  "org.scalameta"  %% "munit-scalacheck"  % "0.7.20" % Test
)

testFrameworks += new TestFramework("minitest.runner.Framework")
testFrameworks += new TestFramework("munit.Framework")

doctestDecodeHtmlEntities := true
