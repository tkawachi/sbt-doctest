import sbt._, Keys._
import sbtrelease._
import xerial.sbt.Sonatype._
import ReleaseStateTransformations._
import com.typesafe.sbt.SbtScalariform.scalariformSettings
import com.typesafe.sbt.pgp.PgpKeys
import sbtbuildinfo.Plugin._

object build extends Build {

  def runTaskStep[A](task: TaskKey[A]) =
    ReleaseStep(state => Project.extract(state).runTask(task, state)._1)

  lazy val commonSettings = sonatypeSettings ++ ReleasePlugin.releaseSettings ++ /*scalariformSettings ++ */ Seq(
    organization := "com.github.tkawachi",
    licenses := Seq("MIT" -> url("http://opensource.org/licenses/MIT")),
    scmInfo := Some(ScmInfo(
      url("https://github.com/tkawachi/sbt-doctest/"),
      "scm:git:github.com:tkawachi/sbt-doctest.git"
    )),
    ReleasePlugin.ReleaseKeys.releaseProcess := Seq[ReleaseStep](
      checkSnapshotDependencies,
      inquireVersions,
      runClean,
      runTest,
      setReleaseVersion,
      commitReleaseVersion,
      tagRelease,
      runTaskStep(PgpKeys.publishSigned),
      setNextVersion,
      commitNextVersion,
      runTaskStep(SonatypeKeys.sonatypeReleaseAll),
      pushChanges
    ),
    pomExtra := {
      <url>https://github.com/tkawachi/sbt-doctest/</url>
      <developers>
        <developer>
          <id>kawachi</id>
          <name>Takashi Kawachi</name>
          <url>https://github.com/tkawachi</url>
        </developer>
      </developers>
    }
  )

  lazy val interface = Project("interface", file("interface")).settings(
    commonSettings : _*
  ).settings(
    name := "sbt-doctest-interface",
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.2.0" % "test",
      "org.scalacheck" %% "scalacheck" % "1.11.4" % "test",
      "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided"
    ),
    crossVersion := CrossVersion.full,
    crossScalaVersions := Seq("2.10.4", "2.11.0", "2.11.1")
  ).dependsOn(api)

  lazy val api = Project("api", file("api")).settings(
    commonSettings : _*
  ).settings(
    name := "sbt-doctest-api",
    autoScalaLibrary := false,
    crossPaths := false,
    sourceGenerators in Compile += task{
      val files = ((sourceDirectory in Compile).value / "adt").*("*").get.toArray
      val out = (sourceManaged in Compile).value / "adt"
      val args = Array(
        "com.github.tkawachi.doctest", out
      ) ++ files
      xsbt.datatype.GenerateDatatypes.main(args.map(_.toString))
      (out ** "*.java").get
    }
  )

  lazy val plugin: Project = Project("plugin", file("plugin")).settings(
    commonSettings ++ buildInfoSettings ++ ScriptedPlugin.scriptedSettings ++ Seq("api", "plugin").map(module =>
      ScriptedPlugin.scripted <<= ScriptedPlugin.scripted.dependsOn(publishLocal in LocalProject(module))
    ) : _*
  ).settings(
    sbtPlugin := true,
    name := "sbt-doctest",
    libraryDependencies ++= Seq(
      "commons-io" % "commons-io" % "2.4"
    ),
    buildInfoKeys := Seq[BuildInfoKey](name, version, scalaVersion, organization),
    buildInfoPackage := "com.github.tkawachi.doctest",
    buildInfoObject := "SbtDoctestInfo",
    sourceGenerators in Compile <+= buildInfo,
    ScriptedPlugin.scriptedLaunchOpts ++= sys.process.javaVmArguments.filter(
      a => Seq("-Xmx", "-Xms", "-XX").exists(a.startsWith)
    ),
    ScriptedPlugin.scriptedLaunchOpts += ("-Dplugin.version=" + version.value),
    ScriptedPlugin.scriptedBufferLog := false
  ).dependsOn(api)

  lazy val root = Project("root", file(".")).settings(
    commonSettings : _*
  ).settings(
    publishArtifact := false,
    publish := {},
    publishLocal := {}
  ).aggregate(interface, api, plugin)

}
