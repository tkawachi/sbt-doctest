package com.github.tkawachi.doctest

import org.scalatest.{ Matchers, FunSpec }
import scala.io.Source

class ExtractorSpec extends FunSpec with Matchers {
  it("extract") {
    val src = Source.fromFile("src/test/resources/Test.scala").mkString
    val extractor = new Extractor
    extractor.extract(src) should equal(List(
      Example(None, "new Test().f(10)", "20", 8),
      Example(None, """"hello, " + "world!"""", "hello, world!", 11)
    ))
  }
}
