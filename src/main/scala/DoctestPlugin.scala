import com.github.tkawachi.doctest.{ TestFramework => TFramework, ScalaCheck, Specs2, ScalaTest, TestGenerator }
import sbt._, Keys._

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
object DoctestPlugin extends Plugin {
  val doctestTestFramework = settingKey[String]("Test framework. Specify scalatest (default) or specs2.")
  val doctestWithDependencies = settingKey[Boolean]("Whether to include libraryDependencies to doctestSettings.")
  val doctestGenTests = taskKey[Seq[File]]("Generates test files.")

  /**
   * Default libraryDependencies.
   */
  object TestLibs {
    val scalacheck = Seq("org.scalacheck" %% "scalacheck" % "1.11.6" % "test")

    val scalatest = Seq("org.scalatest" %% "scalatest" % "2.2.0" % "test") ++ scalacheck

    val specs2Version = "2.4.4"

    val specs2 = Seq(
      "org.specs2" %% "specs2-core" % specs2Version % "test",
      "org.specs2" %% "specs2-scalacheck" % specs2Version % "test"
    )
  }

  /**
   * Settings for test Generation.
   */
  val doctestGenSettings = Seq(
    doctestTestFramework := "scalatest",
    doctestWithDependencies := true,
    doctestGenTests := {
      (managedSourceDirectories in Test).value.headOption match {
        case None =>
          streams.value.log.warn("managedSourceDirectories in Test is empty. Failed to generate tests")
          Seq()
        case Some(testDir) =>
          (unmanagedSources in Compile).value
            .filter(_.ext == "scala")
            .flatMap(TestGenerator(_, TFramework(doctestTestFramework.value)))
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
    sourceGenerators in Test += doctestGenTests.taskValue
  )

  val doctestSettings = doctestGenSettings ++ Seq(
    libraryDependencies ++= (if (doctestWithDependencies.value) {
      TFramework(doctestTestFramework.value) match {
        case ScalaTest => TestLibs.scalatest
        case Specs2 => TestLibs.specs2
        case ScalaCheck => TestLibs.scalacheck
      }
    } else {
      Seq.empty
    })
  )
}
