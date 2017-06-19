package com.github.tkawachi.doctest

import scala.io.Source
import utest._

object ScaladocExtractorSpec extends TestSuite {

  val tests = this{

    val extractor = new ScaladocExtractor

    "extracts from Test.scala" - {
      val src = Source.fromFile("src/test/resources/Test.scala").mkString
      val actual = extractor.extract(src)
      val expected =
        List(
          ScaladocComment(None, "Test", "/**\n * Test class\n */", 1),
          ScaladocComment(None, "f",
            """/**
              |   * A function.
              |   *
              |   * >>> new Test().f(10)
              |   * 20
              |   *
              |   * >>> "hello, " + "world!"
              |   * hello, world!
              |   */""".stripMargin, 5),
          ScaladocComment(None, "+=",
            """/** Ascii method
              |   * scala> new Test() += 1
              |   * 2
              |   */""".stripMargin, 16)
        )
      assert(expected == actual)
    }

    "extracts from RootPackage.scala" - {
      val src = Source.fromFile("src/test/resources/RootPackage.scala").mkString
      val actual = extractor.extract(src)
      val expected =
        List(
          ScaladocComment(None, "Root1", "/** Class comment */", 1),
          ScaladocComment(None, "method", "/** Method comment */", 3),
          ScaladocComment(Some("a1"), "A1", "/** A1 */", 8),
          ScaladocComment(Some("a1.b1"), "B1", "/** B1 */", 11),
          ScaladocComment(None, "Root2", "/** Root2 */", 16),
          ScaladocComment(None, "method2", "/** Method2 comment */", 20),
          ScaladocComment(None, "IntAlias", "/** Type alias */", 23)
        )
      assert(expected == actual)
    }

    "extracts from Package.scala" - {
      val src = Source.fromFile("src/test/resources/Package.scala").mkString
      val actual = extractor.extract(src)
      val expected =
        List(
          ScaladocComment(Some("some.pkg"), "Root1", "/** Class comment */", 3),
          ScaladocComment(Some("some.pkg"), "method", "/** Method comment */", 5),
          ScaladocComment(Some("some.pkg.a1"), "A1", "/** A1 */", 10),
          ScaladocComment(Some("some.pkg.a1.b1"), "B1", "/** B1 */", 13),
          ScaladocComment(Some("some.pkg"), "Root2", "/** Root2 */", 18)
        )
      assert(expected == actual)
    }

  }

}
