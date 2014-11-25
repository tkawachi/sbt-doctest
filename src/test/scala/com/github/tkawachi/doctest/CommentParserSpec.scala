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
      parse(comment).get should equal(List(Example("1 + 2", TestResult("3"), 1)))
    }

    it("parses a multi-lines expr") {
      val comment =
        """ * >>> 1 + 2 +
          | * ... 3 +
          | * ... 4 + 5
          | * 15
        """.stripMargin
      parse(comment).get should equal(List(Example(s"1 + 2 +${LS}3 +${LS}4 + 5", TestResult("15"), 1)))
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
        List(
          Example("1 + 2", TestResult("3"), 1),
          Example("\"Hello,\" + \" world\"", TestResult("Hello, world"), 4))
      )
    }

    it("parses a multi-line output") {
      val comment =
        """ * >>> "abc\ndef"
          | * abc
          | * def
          | *
        """.stripMargin
      parse(comment).get should equal(
        List(Example("\"abc\\ndef\"", TestResult("abc\ndef", None), 1))
      )
    }

    it("parses a <BLANKLINE>") {
      val comment =
        """ * >>> "abc\n\ndef"
          | * abc
          | * <BLANKLINE>
          | * def
        """.stripMargin
      parse(comment).get should equal(
        List(Example("\"abc\\n\\ndef\"", TestResult("abc\n\ndef", None), 1))
      )
    }

    it("parses }}} as an end of example") {
      val comment =
        """ * >>> 1 + 1
          | * 2
          | * }}}
         """.stripMargin
      parse(comment).get should equal(
        List(Example("1 + 1", TestResult("2", None), 1))
      )
    }

    it("parses }}} as an end of multiline example") {
      val comment =
        """ * >>> "Hello\nWorld"
          | * Hello
          | * World
          | * }}}
        """.stripMargin
      parse(comment).get should equal(
        List(Example("\"Hello\\nWorld\"", TestResult("Hello\nWorld", None), 1))
      )
    }

    it("parses multi-line outputs") {
      val comment =
        """ * >>> "abc\ndef"
          | * abc
          | * def
          | *
          | * >>> " abc\ndef"
          | * " abc
          | * def"
        """.stripMargin
      parse(comment).get should equal(
        List(
          Example("\"abc\\ndef\"", TestResult("abc\ndef", None), 1),
          Example("\" abc\\ndef\"", TestResult("\" abc\ndef\"", None), 5)
        )
      )
    }

    it("parses an import line") {
      val comment =
        """ * >>> import abc.def
        """.stripMargin
      parse(comment).get should equal(List(Verbatim("import abc.def", 1)))
    }

    it("parses a single-line assignment") {
      val comment =
        """ * >>> val xs = List(1, 2, 3)
        """.stripMargin
      parse(comment).get should equal(List(Verbatim("val xs = List(1, 2, 3)", 1)))
    }

    it("parses a multi-line assignment") {
      val comment =
        """ * >>> val xs = List(
          | * ... 1,
          | * ... 2,
          | * ... 3)
        """.stripMargin
      parse(comment).get should equal(List(Verbatim(s"val xs = List(${LS}1,${LS}2,${LS}3)", 1)))
    }
  }

  describe("Scala repl style") {
    it("parses a single example") {
      val comment =
        """ * scala> 1 + 2
          | * res0: Int = 3
        """.stripMargin
      parse(comment).get should equal(List(Example("1 + 2", TestResult("3", Some("Int")), 1)))
    }

    it("parses a multi-lines expr") {
      val comment =
        """ * scala> 1 + 2 +
          | *      | 3 +
          | *      | 4 + 5
          | * res0: Int = 15
        """.stripMargin
      parse(comment).get should equal(List(Example(s"1 + 2 +${LS}3 +${LS}4 + 5", TestResult("15", Some("Int")), 1)))
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
        List(
          Example("1 + 2", TestResult("3", Some("Int")), 1),
          Example("\"Hello,\" + \" world\"", TestResult("Hello, world", Some("String")), 4))
      )
    }

    it("parses a multi-line output") {
      val comment =
        """ * scala> "abc\ndef"
          | * res0: String =
          | * abc
          | * def
        """.stripMargin
      parse(comment).get should equal(
        List(Example("\"abc\\ndef\"", TestResult("abc\ndef", Some("String")), 1))
      )
    }

    it("parses a <BLANKLINE>") {
      val comment =
        """ * scala> "abc\n\ndef"
          | * res0: String =
          | * abc
          | * <BLANKLINE>
          | * def
        """.stripMargin
      parse(comment).get should equal(
        List(Example("\"abc\\n\\ndef\"", TestResult("abc\n\ndef", Some("String")), 1))
      )
    }

    it("parses }}} as an end of example") {
      val comment =
        """ * scala> 1 + 1
          | * res0: Int = 2
          | * }}}
        """.stripMargin
      parse(comment).get should equal(
        List(Example("1 + 1", TestResult("2", Some("Int")), 1))
      )
    }

    it("parses }}} as an end of multiline example") {
      val comment =
        """ * scala> "Hello\nWorld"
          | * res0: String =
          | * Hello
          | * World
          | * }}}
        """.stripMargin
      parse(comment).get should equal(
        List(Example("\"Hello\\nWorld\"", TestResult("Hello\nWorld", Some("String")), 1))
      )
    }

    it("parses multi-line outputs") {
      val comment =
        """ * scala> "abc\ndef"
          | * res0: String =
          | * abc
          | * def
          | *
          | * scala> " abc\ndef"
          | * res2: String =
          | * " abc
          | * def"
        """.stripMargin
      parse(comment).get should equal(
        List(
          Example("\"abc\\ndef\"", TestResult("abc\ndef", Some("String")), 1),
          Example("\" abc\\ndef\"", TestResult("\" abc\ndef\"", Some("String")), 6)
        )
      )
    }

    it("parses an import line") {
      val comment =
        """ * scala> import abc.def
        """.stripMargin
      parse(comment).get should equal(List(Verbatim("import abc.def", 1)))
    }

    it("parses a result with a parametric type") {
      val comment =
        """ * scala> List(1)
          | * res1: List[Int] = List(1)
        """.stripMargin
      parse(comment).get should equal(List(Example("List(1)", TestResult("List(1)", Some("List[Int]")), 1)))
    }

    it("parses a type with a colon") {
      val comment =
        """ * scala> =:=
          | * res0: =:=.type = scala.Predef...
        """.stripMargin
      parse(comment).get should equal(List(Example("=:=", TestResult("scala.Predef...", Some("=:=.type")), 1)))
    }

    it("parses a type with equal signs") {
      val comment =
        """ * scala> ==>>.empty[Int, String]
          | * res0: ==>>[Int, String] = ???
        """.stripMargin
      parse(comment).get should equal(
        List(Example("==>>.empty[Int, String]", TestResult("???", Some("==>>[Int, String]")), 1))
      )
    }

    it("parses a type which only contains equal signs") {
      // This example would compile with this alias definition:
      // type == = String
      val comment =
        """ * scala> "Hello\nWorld": ==
          | * res0: == =
          | * Hello
          | * World
        """.stripMargin
      parse(comment).get should equal(
        List(Example("\"Hello\\nWorld\": ==", TestResult("Hello\nWorld", Some("==")), 1))
      )
    }

    it("parses a single-line assignment") {
      val comment =
        """ * scala> var xs = List(1, 2, 3)
        """.stripMargin
      parse(comment).get should equal(List(Verbatim("var xs = List(1, 2, 3)", 1)))
    }

    it("parses a multi-line assignment") {
      val comment =
        """ * scala> var ys = List(
          | *      |   1,
          | *      |   2,
          | *      |   3)
        """.stripMargin
      parse(comment).get should equal(List(Verbatim(s"var ys = List($LS  1,$LS  2,$LS  3)", 1)))
    }

    it("parses an example that starts with a verbatim keyword") {
      val comment =
        """ * scala> val value = 1
          | * scala> value + 1
          | * res0: Int = 2
        """.stripMargin
      parse(comment).get should equal(
        List(
          Verbatim("val value = 1", 1),
          Example("value + 1", TestResult("2", Some("Int")), 2))
      )
    }
  }

  describe("Property based") {
    it("parses a single property") {
      val comment =
        """ * prop> (i: Int) => i + i == i * 2
        """.stripMargin
      parse(comment).get should equal(List(Property("""(i: Int) => i + i == i * 2""", 1)))
    }

    it("parses a muti-lines property") {
      val comment =
        """ * prop> (i: Int) =>
          | *     | i + i == (i *
          | *     | 2)
          | *
        """.stripMargin
      parse(comment).get should equal(List(Property(s"(i: Int) =>${LS}i + i == (i *${LS}2)", 1)))
    }

    it("parses an import line") {
      val comment =
        """ * prop> import abc.def
        """.stripMargin
      parse(comment).get should equal(List(Verbatim("import abc.def", 1)))
    }

    it("parses a single-line assignment") {
      val comment =
        """ * prop> var xs = List(1, 2, 3)
        """.stripMargin
      parse(comment).get should equal(List(Verbatim("var xs = List(1, 2, 3)", 1)))
    }

    it("parses a multi-line assignment") {
      val comment =
        """ * prop> var ys = List(
          | *     |  1,
          | *     |   2,
          | *     |    3)
        """.stripMargin
      parse(comment).get should equal(List(Verbatim(s"var ys = List($LS 1,$LS  2,$LS   3)", 1)))
    }

    it("parses a multi-line assignment whose first line is only a keyword") {
      val comment =
        """ * prop> def
          | *     | x = 1
        """.stripMargin
      parse(comment).get should equal(List(Verbatim(s"def${LS}x = 1", 1)))
    }
  }

  describe("No examples") {
    it("returns None") {
      val comment =
        """/**
          | * Test comment.
          | */
        """.stripMargin
      parse(comment).get should equal(List.empty)
    }
  }
}
