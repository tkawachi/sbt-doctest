crossScalaVersions := Seq("2.11.12", "2.12.12")

javacOptions ++= (scalaVersion.value match {
  case v if v startsWith "2.13." => Seq("-source", "1.8", "-target", "1.8")
  case v if v startsWith "2.12." => Seq("-source", "1.8", "-target", "1.8")
  case v if v startsWith "2.11." => Seq("-source", "1.8", "-target", "1.6")
})

scalacOptions         := Seq("-Ywarn-dead-code")
scalacOptions in Test -= "-Ywarn-dead-code"
scalacOptions        ++= (scalaVersion.value match {
  case v if v startsWith "2.13." => Seq("-target:jvm-1.8")
  case v if v startsWith "2.12." => Seq("-target:jvm-1.8", "-opt:l:method")
  case v if v startsWith "2.11." => Seq("-target:jvm-1.6")
})

// Declares scalatest, scalacheck, minitest and utest dependencies explicitly.
libraryDependencies ++= Seq(
  "com.lihaoyi"    %% "utest"             % "0.6.4"  % Test,
  "org.scalatest"  %% "scalatest"         % "3.0.9"  % Test,
  "org.scalacheck" %% "scalacheck"        % "1.15.3" % Test,
  "io.monix"       %% "minitest"          % "2.8.2"  % Test,
  "io.monix"       %% "minitest-laws"     % "2.8.2"  % Test,
  "org.specs2"     %% "specs2-core"       % "4.10.6"  % Test,
  "org.specs2"     %% "specs2-scalacheck" % "4.10.6"  % Test,
  "org.scalameta"  %% "munit-scalacheck"  % "0.7.20"  % Test,
  "org.scalameta"  %% "munit-scalacheck"  % "0.7.20"  % Test
)

testFrameworks += new TestFramework("minitest.runner.Framework")
testFrameworks += new TestFramework("munit.Framework")

doctestDecodeHtmlEntities := true
