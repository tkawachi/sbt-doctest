import sbtrelease._
import ReleaseStateTransformations._

sonatypeSettings

releaseSettings

def runTaskStep[A](task: TaskKey[A]) =
  ReleaseStep(state => Project.extract(state).runTask(task, state)._1)

ReleaseKeys.releaseProcess := Seq[ReleaseStep](
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
)

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
