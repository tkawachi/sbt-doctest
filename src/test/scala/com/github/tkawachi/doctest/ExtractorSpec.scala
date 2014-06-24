package com.github.tkawachi.doctest

import org.scalatest.{ BeforeAndAfter, Matchers, FunSpec }
import scala.io.Source

class ExtractorSpec extends FunSpec with Matchers with BeforeAndAfter {

  val extractor = new Extractor

  it("extracts from Test.scala") {
    val src = Source.fromFile("src/test/resources/Test.scala").mkString
    extractor.extract(src) should equal(
      List(
        ScaladocComment(None, "/**\n * Test class\n */", 1),
        ScaladocComment(None,
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
    val src = Source.fromFile("src/test/resources/RootPackage.scala").mkString
    extractor.extract(src) should equal(
      List(
        ScaladocComment(None, "/** Class comment */", 1),
        ScaladocComment(None, "/** Method comment */", 3),
        ScaladocComment(Some("a1"), "/** A1 */", 8),
        ScaladocComment(Some("a1.b1"), "/** B1 */", 11),
        ScaladocComment(None, "/** Root2 */", 16)
      )
    )
  }

  it("extracts from Package.scala") {
    val src = Source.fromFile("src/test/resources/Package.scala").mkString
    extractor.extract(src) should equal(
      List(
        ScaladocComment(Some("some.pkg"), "/** Class comment */", 3),
        ScaladocComment(Some("some.pkg"), "/** Method comment */", 5),
        ScaladocComment(Some("some.pkg.a1"), "/** A1 */", 10),
        ScaladocComment(Some("some.pkg.a1.b1"), "/** B1 */", 13),
        ScaladocComment(Some("some.pkg"), "/** Root2 */", 18)
      )
    )
  }

}
