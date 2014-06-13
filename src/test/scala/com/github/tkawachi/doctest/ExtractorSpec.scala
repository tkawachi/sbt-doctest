package com.github.tkawachi.doctest

import com.github.tkawachi.doctest.CommentParser.Extracted
import org.scalatest.{ BeforeAndAfter, Matchers, FunSpec }
import scala.io.Source

class ExtractorSpec extends FunSpec with Matchers with BeforeAndAfter {

  val extractor = new Extractor

  it("extract") {
    val src = Source.fromFile("src/test/resources/Test.scala").mkString
    extractor.extract(src) should equal(
      List(
        ParsedDoctest(
          None,
          List(Extracted("new Test().f(10)", "20", 4),
            Extracted("\"hello, \" + \"world!\"", "hello, world!", 7)
          ),
          5)
      )
    )
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
      extractor.extractFromComment(Some("abc"), comment, 10) should equal(
        Some(
          ParsedDoctest(
            Some("abc"),
            List(
              Extracted("1 + 3", "4", 5),
              Extracted("10 + 1", "11", 9)
            ),
            10
          )
        )
      )
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
      extractor.extractFromComment(Some("abc"), comment, 10) should be(None)
    }
  }
}
