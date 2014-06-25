package com.github.tkawachi.doctest

import java.io.File

import org.scalatest.{ BeforeAndAfter, Matchers, FunSpec }
import scala.io.Source

class ExtractorSpec extends FunSpec with Matchers with BeforeAndAfter {

  val extractor = new Extractor


  def readResourceFile(name: String): String =
  Source.fromFile("interface/src/test/resources/" + name).mkString

  it("extracts from Test.scala") {
    val src = readResourceFile("Test.scala")
    extractor.extract(src) should equal(
      List(
        new ScaladocComment("", "/**\n * Test class\n */", 1),
        new ScaladocComment("",
          """/**
          |   * A function.
          |   *
          |   * >>> new Test().f(10)
          |   * 20
          |   *
          |   * >>> "hello, " + "world!"
          |   * hello, world!
          |   */""".stripMargin, 5)
      )
    )
  }

  it("extracts from RootPackage.scala") {
    val src = readResourceFile("RootPackage.scala")
    extractor.extract(src) should equal(
      List(
        new ScaladocComment("", "/** Class comment */", 1),
        new ScaladocComment("", "/** Method comment */", 3),
        new ScaladocComment("a1", "/** A1 */", 8),
        new ScaladocComment("a1.b1", "/** B1 */", 11),
        new ScaladocComment("", "/** Root2 */", 16)
      )
    )
  }

  it("extracts from Package.scala") {
    val src = readResourceFile("Package.scala")
    extractor.extract(src) should equal(
      List(
        new ScaladocComment("some.pkg", "/** Class comment */", 3),
        new ScaladocComment("some.pkg", "/** Method comment */", 5),
        new ScaladocComment("some.pkg.a1", "/** A1 */", 10),
        new ScaladocComment("some.pkg.a1.b1", "/** B1 */", 13),
        new ScaladocComment("some.pkg", "/** Root2 */", 18)
      )
    )
  }

}
