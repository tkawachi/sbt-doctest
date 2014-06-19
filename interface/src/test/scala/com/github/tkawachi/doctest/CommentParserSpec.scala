package com.github.tkawachi.doctest

import org.scalatest.{ Matchers, FunSpec }

class CommentParserSpec extends FunSpec with Matchers {
  import CommentParser.parse

  val LS = System.lineSeparator()

  describe("Python style") {
    it("parses a single example") {
      val comment =
        """ * >>> 1 + 2
          | * 3
        """.stripMargin
      parse(comment).get should equal(List(new Example("1 + 2", "3", 1)))
    }

    it("parses a multi-lines expr") {
      val comment =
        """ * >>> 1 + 2 +
          | * ... 3 +
          | * ... 4 + 5
          | * 15
        """.stripMargin
      parse(comment).get should equal(List(new Example(s"1 + 2 +${LS}3 +${LS}4 + 5", "15", 1)))
    }

    it("doesn't parse a single example for different leading string") {
      val comment =
        """ * >>> 1 + 2
          |*  3
        """.stripMargin
      parse(comment).get should equal(Nil)
    }

    it("parses multiple examples") {
      val comment =
        """ * >>> 1 + 2
          | * 3
          | *
          | * >>> "Hello," + " world"
          | * Hello, world
        """.stripMargin
      parse(comment).get should equal(
        List(new Example("1 + 2", "3", 1), new Example("\"Hello,\" + \" world\"", "Hello, world", 4))
      )
    }

    it("parses an import line") {
      val comment =
        """ * >>> import abc.def
        """.stripMargin
      parse(comment).get should equal(List(new Import("import abc.def")))
    }
  }

  describe("Scala repl style") {
    it("parses a single example") {
      val comment =
        """ * scala> 1 + 2
          | * res0: Int = 3
        """.stripMargin
      parse(comment).get should equal(List(new Example("1 + 2", "3", 1)))
    }

    it("parses a multi-lines expr") {
      val comment =
        """ * scala> 1 + 2 +
          | *      | 3 +
          | *      | 4 + 5
          | * res0: Int = 15
        """.stripMargin
      parse(comment).get should equal(List(new Example(s"1 + 2 +${LS}3 +${LS}4 + 5", "15", 1)))
    }

    it("doesn't parse a single example for different leading string") {
      val comment =
        """ * scala> 1 + 2
          |*  3
        """.stripMargin
      parse(comment).get should equal(Nil)
    }

    it("parses multiple examples") {
      val comment =
        """ * scala> 1 + 2
          | * res1: Int = 3
          | *
          | * scala> "Hello," + " world"
          | * res2: String = Hello, world
        """.stripMargin
      parse(comment).get should equal(
        List(new Example("1 + 2", "3", 1), new Example("\"Hello,\" + \" world\"", "Hello, world", 4))
      )
    }

    it("parses an import line") {
      val comment =
        """ * scala> import abc.def
        """.stripMargin
      parse(comment).get should equal(List(new Import("import abc.def")))
    }
  }

  describe("Property based") {
    it("parses a single property") {
      val comment =
        """ * prop> (i: Int) => i + i should === i * 2
        """.stripMargin
      parse(comment).get should equal(List(new Prop("""(i: Int) => i + i should === i * 2""", 1)))
    }

    it("parses a muti-lines property") {
      val comment =
        """ * prop> (i: Int) =>
          | *     | i + i should === (i *
          | *     | 2)
          | *
        """.stripMargin
      parse(comment).get should equal(List(new Prop(s"(i: Int) =>${LS}i + i should === (i *${LS}2)", 1)))
    }

    it("parses an import line") {
      val comment =
        """ * prop> import abc.def
        """.stripMargin
      parse(comment).get should equal(List(new Import("import abc.def")))
    }
  }
}
