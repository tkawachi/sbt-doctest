import com.github.tkawachi.doctest.TestGenerator
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
  val doctestGenTests = taskKey[Seq[File]]("Generates test files.")

  val doctestSettings = Seq(
    doctestGenTests := {
      (managedSourceDirectories in Test).value.headOption match {
        case None =>
          streams.value.log.warn("managedSourceDirectories in Test is empty. Failed to generate tests")
          Seq()
        case Some(testDir) =>
          (unmanagedSources in Compile).value
            .filter(_.ext == "scala")
            .flatMap(TestGenerator.apply)
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
    sourceGenerators in Test += doctestGenTests.taskValue,
    libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.2.0" % "test",
      "org.scalacheck" %% "scalacheck" % "1.11.4" % "test"
    )
  )
}
