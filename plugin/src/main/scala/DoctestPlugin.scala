import com.github.tkawachi.doctest.{ ParsedDoctest, TestGenerator, SbtDoctestInfo }
import org.apache.commons.io.FilenameUtils
import sbt._, Keys._

/**
 * Sbt plugin for doctest.
 *
 * >>> 1 + 1
 * 2
 *
 * scala> 1 + 10
 * res0: Int = 11
 */
object DoctestPlugin extends Plugin {
  val doctestGenTests = taskKey[Seq[File]]("Generates test files.")

  private val extract = TaskKey[Seq[(String, Seq[ParsedDoctest])]]("doctest-extract")
  private val mainClassLoader = TaskKey[ClassLoader]("main-classloader")

  private def invoke[A](clazz: Class[_], method: String, params: AnyRef*): A = {
    val receiver = clazz.getConstructor().newInstance()
    clazz.getMethod(method, params.map(_.getClass): _*).invoke(receiver, params: _*).asInstanceOf[A]
  }

  private def deserialize[A](bytes: Array[Byte]): A = {
    val bais = new java.io.ByteArrayInputStream(bytes)
    val os = new java.io.ObjectInputStream(bais)
    os.readObject().asInstanceOf[A]
  }

  val doctestSettings = Seq(
    mainClassLoader <<= (fullClasspath in Compile, scalaInstance).map((path, instance) =>
      classpath.ClasspathUtilities.makeLoader(path.map(_.data), instance)
    ),
    extract := {
      val c = mainClassLoader.value.loadClass("com.github.tkawachi.doctest.Extractor")
      (sources in Compile).value
        .filter(_.ext == "scala")
        .map{ srcFile =>
          val src = scala.io.Source.fromFile(srcFile).mkString
          val basename = FilenameUtils.getBaseName(srcFile.getName)
          val tests = invoke[Array[Byte]](c, "extractBytes", src)
          basename -> deserialize[Array[ParsedDoctest]](tests).toList
        }
    },
    doctestGenTests := {
      val testDir = (sourceManaged in Test).value
      extract.value.flatMap{ case (basename, tests) =>
        TestGenerator(basename, tests)
      }.groupBy{ r =>
        r.pkg -> r.basename
      }.flatMap {
        case ((pkg, basename), results) =>
          results.zipWithIndex.map {
            case (result, idx) =>
              val writeBasename = if (idx == 0) basename else basename + idx
              val writeDir = pkg.split("\\.").foldLeft(testDir) { (a: File, e: String) => new File(a, e) }
              val writeFile = new File(writeDir, writeBasename + "Doctest.scala")
              IO.write(writeFile, result.testSource)
              writeFile
          }
      }.toSeq
    },
    sourceGenerators in Test += doctestGenTests.taskValue,
    libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.0" % "test",
    libraryDependencies += SbtDoctestInfo.organization %% "sbt-doctest-interface" % SbtDoctestInfo.version cross CrossVersion.full
  )
}
