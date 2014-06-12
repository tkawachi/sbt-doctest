package com.github.tkawachi.doctest

import org.scalatest.{ BeforeAndAfter, Matchers, FunSpec }
import scala.io.Source

class ExtractorSpec extends FunSpec with Matchers with BeforeAndAfter {

  val extractor = new Extractor

  it("extract") {
    val src = Source.fromFile("src/test/resources/Test.scala").mkString
    extractor.extract(src) should equal(List(
      Example(None, "new Test().f(10)", "20", 8),
      Example(None, """"hello, " + "world!"""", "hello, world!", 11)
    ))
  }

  describe("extractFromComment") {
    it("extracts ex1") {
      val comment =
        """/**
          | * Sample Comment
          | *
          | * {{{
          | * >>> 1 + 3
          | * 4
          | * }}}
          | *
          | * >>> 10 + 1
          | * 11
          | */
          """.stripMargin
      extractor.extractFromComment(Some("abc"), comment, 10) should equal(Seq(
        Example(Some("abc"), "1 + 3", "4", 14),
        Example(Some("abc"), "10 + 1", "11", 18)
      ))
    }

    it("skips an example when no expectation line") {
      val comment =
        """/**
          | * Sample Comment
          | *
          | * >>> 10 + 1
          | *
          | */
        """.stripMargin
      extractor.extractFromComment(Some("abc"), comment, 10) should equal(Seq())
    }
  }
}
