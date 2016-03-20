package com.github.tkawachi.doctest
import sbt._, Keys._
import sbt.plugins.JvmPlugin

/**
 * Sbt plugin for doctest.
 *
 * It enables doctest like following.
 *
 * {{{
 * # In Python style
 * >>> 1 + 1
 * 2
 *
 * # In scala repl style
 * scala> 1 + 10
 * res0: Int = 11
 * }}}
 */
object DoctestPlugin extends AutoPlugin {
  self =>

  sealed abstract class DoctestTestFramework

  object DoctestTestFramework {
    case object Specs2 extends DoctestTestFramework
    case object ScalaTest extends DoctestTestFramework
    case object ScalaCheck extends DoctestTestFramework
  }
  import DoctestTestFramework._

  override def trigger: PluginTrigger = allRequirements

  // Currently not working with ScalaJS. See #52.
  override def requires = JvmPlugin

  override def projectSettings: Seq[Setting[_]] = doctestSettings

  object autoImport {
    val doctestTestFramework = settingKey[DoctestTestFramework]("Test framework. Specify ScalaCheck (default), Specs2 or ScalaTest.")
    val doctestWithDependencies = settingKey[Boolean]("Whether to include libraryDependencies to doctestSettings.")
    val doctestGenTests = taskKey[Seq[File]]("Generates test files.")
    val doctestDecodeHtmlEntities = settingKey[Boolean]("Whether to decode HTML entities.")

    val DoctestTestFramework = self.DoctestTestFramework
  }

  import autoImport._

  /**
   * Default libraryDependencies.
   */
  object TestLibs {
    val scalacheck = Seq("org.scalacheck" %% "scalacheck" % DoctestBuildinfo.scalacheckVersion % "test")
    val scalatest = Seq("org.scalatest" %% "scalatest" % DoctestBuildinfo.scalatestVersion % "test") ++ scalacheck
    val specs2 = Seq(
      "org.specs2" %% "specs2-core" % DoctestBuildinfo.specs2Version % "test",
      "org.specs2" %% "specs2-scalacheck" % DoctestBuildinfo.specs2Version % "test"
    )
  }

  /**
   * Settings for test Generation.
   */
  val doctestGenSettings = Seq(
    doctestTestFramework := (doctestTestFramework ?? ScalaCheck).value,
    doctestWithDependencies := (doctestWithDependencies ?? true).value,
    doctestDecodeHtmlEntities := (doctestDecodeHtmlEntities ?? false).value,
    doctestGenTests := {
      (managedSourceDirectories in Test).value.headOption match {
        case None =>
          streams.value.log.warn("managedSourceDirectories in Test is empty. Failed to generate tests")
          Seq()
        case Some(testDir) =>
          val srcEncoding = TestGenerator.findEncoding((scalacOptions in Compile).value).getOrElse("UTF-8")
          (unmanagedSources in Compile).value
            .filter(_.ext == "scala")
            .flatMap(TestGenerator(_, srcEncoding, doctestTestFramework.value, doctestDecodeHtmlEntities.value))
            .groupBy(r => r.pkg -> r.basename)
            .flatMap {
              case ((pkg, basename), results) =>
                results.zipWithIndex.map {
                  case (result, idx) =>
                    val writeBasename = if (idx == 0) basename else basename + idx
                    val writeDir = pkg.fold(testDir)(_.split("\\.").foldLeft(testDir) { (a: File, e: String) => new File(a, e) })
                    val writeFile = new File(writeDir, writeBasename + "Doctest.scala")
                    IO.write(writeFile, result.testSource)
                    writeFile
                }
            }.toSeq
      }
    },
    sourceGenerators in Test <+= doctestGenTests
  )

  val doctestSettings = doctestGenSettings ++ Seq(
    libraryDependencies ++= (if (doctestWithDependencies.value) {
      doctestTestFramework.value match {
        case ScalaTest => TestLibs.scalatest
        case Specs2 => TestLibs.specs2
        case ScalaCheck => TestLibs.scalacheck
      }
    } else {
      Seq.empty
    })
  )
}
