package com.github.tkawachi.doctest

import org.scalatest.{ BeforeAndAfter, Matchers, FunSpec }
import scala.io.Source

class MarkdownCodeblocksExtractorSpec extends FunSpec with Matchers with BeforeAndAfter {

  val extractor = new MarkdownCodeblocksExtractor

  it("extracts Markdown code block") {
    val src = """
              | # Header
              |
              |```scala
              | scala> println("Hello, World!")
              | Hello, World!
              | ```""".stripMargin

    extractor.extract(src) should contain(
      MarkdownCodeblock(
        """```scala
         | scala> println("Hello, World!")
         | Hello, World!
         | ```""".stripMargin, 4
      )
    )
  }

  it("extracts multiple Markdown code blocks") {
    val src = """
              | # Header
              |
              |```scala
              | scala> println("Hello, World!")
              | Hello, World!
              | ```
              |
              |```scala
              | scala> println("Good night, World!")
              | Good night, World!
              | ```""".stripMargin

    extractor.extract(src).size should equal(2)
  }
}
