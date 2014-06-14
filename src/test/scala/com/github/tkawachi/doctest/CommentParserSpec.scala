package com.github.tkawachi.doctest

import org.scalatest.{ Matchers, FunSpec }

class CommentParserSpec extends FunSpec with Matchers {
  describe("Python style") {
    it("parses a single example") {
      val comment =
        """ * >>> 1 + 2
          | * 3
        """.stripMargin
      CommentParser.parse(comment).get should equal(List(Example("1 + 2", "3", 1)))
    }

    it("doesn't parse a single example for different leading string") {
      val comment =
        """ * >>> 1 + 2
          |*  3
        """.stripMargin
      CommentParser.parse(comment).get should equal(Nil)
    }

    it("parses multiple examples") {
      val comment =
        """ * >>> 1 + 2
          | * 3
          | *
          | * >>> "Hello," + " world"
          | * Hello, world
        """.stripMargin
      CommentParser.parse(comment).get should equal(
        List(Example("1 + 2", "3", 1), Example("\"Hello,\" + \" world\"", "Hello, world", 4))
      )
    }

    it("parses an import line") {
      val comment =
        """ * >>> import abc.def
        """.stripMargin
      CommentParser.parse(comment).get should equal(List(Import("import abc.def")))
    }
  }

  describe("Scala repl style") {
    it("parses a single example") {
      val comment =
        """ * scala> 1 + 2
          | * res0: Int = 3
        """.stripMargin
      CommentParser.parse(comment).get should equal(List(Example("1 + 2", "3", 1)))
    }

    it("doesn't parse a single example for different leading string") {
      val comment =
        """ * scala> 1 + 2
          |*  3
        """.stripMargin
      CommentParser.parse(comment).get should equal(Nil)
    }

    it("parses multiple examples") {
      val comment =
        """ * scala> 1 + 2
          | * res1: Int = 3
          | *
          | * scala> "Hello," + " world"
          | * res2: String = Hello, world
        """.stripMargin
      CommentParser.parse(comment).get should equal(
        List(Example("1 + 2", "3", 1), Example("\"Hello,\" + \" world\"", "Hello, world", 4))
      )
    }

    it("parses an import line") {
      val comment =
        """ * scala> import abc.def
        """.stripMargin
      CommentParser.parse(comment).get should equal(List(Import("import abc.def")))
    }
  }

  describe("Property based") {
    it("parses a signle property") {
      val comment =
        """ * prop> (i: Int) => i + i should === i * 2
        """.stripMargin
      CommentParser.parse(comment).get should equal(List(Prop("""(i: Int) => i + i should === i * 2""", 1)))
    }
  }
}
