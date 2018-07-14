package com.github.tkawachi.doctest

import scala.io.Source
import utest._

object ScaladocExtractorSpec extends TestSuite {

  val tests = this{

    import ScaladocExtractor.extract

    def extractFromFile(path: String) = extract(Source.fromFile(path).mkString)

    "extracts from Test.scala" - {
      val actual = extractFromFile("src/test/resources/Test.scala")
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
              |   */""".stripMargin, 16),
          ScaladocComment(None, "x",
            """/**
              |    * Doc on val
              |    */""".stripMargin, 22),
          ScaladocComment(None, "z",
            """/**
              |    * Doc on var
              |    */""".stripMargin, 27))
      assert(expected == actual)
    }

    "extracts from RootPackage.scala" - {
      val actual = extractFromFile("src/test/resources/RootPackage.scala")
      val expected =
        List(
          ScaladocComment(None, "Root1", "/** Class comment */", 1),
          ScaladocComment(None, "method", "/** Method comment */", 3),
          ScaladocComment(Some("a1"), "A1", "/** A1 */", 8),
          ScaladocComment(Some("a1.b1"), "B1", "/** B1 */", 11),
          ScaladocComment(None, "Root2", "/** Root2 */", 16),
          ScaladocComment(None, "method2", "/** Method2 comment */", 20),
          ScaladocComment(None, "IntAlias", "/** Type alias */", 23))
      assert(expected == actual)
    }

    "extracts from Package.scala" - {
      val actual = extractFromFile("src/test/resources/Package.scala")
      val expected =
        List(
          ScaladocComment(Some("some.pkg"), "Root1", "/** Class comment */", 3),
          ScaladocComment(Some("some.pkg"), "method", "/** Method comment */", 5),
          ScaladocComment(Some("some.pkg.a1"), "A1", "/** A1 */", 10),
          ScaladocComment(Some("some.pkg.a1.b1"), "B1", "/** B1 */", 13),
          ScaladocComment(Some("some.pkg"), "Root2", "/** Root2 */", 18))
      assert(expected == actual)
    }

    "extracts only Scaladocs with some parseable text" - {
      val source =
        """
          |package all_scaladocs_without_code
          |
          |/**
          | * @version 123
          | * @since 1953
          | */
          |class GotNothing {
          |
          |  /**
          |
          |  */
          |  val a = 0
          |
          |  /**
          |   *
          |   */
          |  var b = 0
          |
          |  /**
          |   */
          |  def c = 0
          |
          |  /**
          |  *
          |  *
          |  *
          |  */
          |  def d = 0
          |
          |  /**
          |   * @param i an Int
          |   * @returns an Int
          |   */
          |  def e(i: Int) = 0
          |
          |  /**
          |   * @deprecated for ever
          |   */
          |  def f = 0
          |
          |  /**
          |   * @todo write code example like
          |   *    require(g == 0)
          |   */
          |  def g = 0
          |
          |  /**
          |  * @constructor
          |  */
          |  def this(i: Int) = this()
          |}
        """.stripMargin

      val actual = extract(source)
      val expected = Nil
      assert(expected == actual)
    }

  }

}
