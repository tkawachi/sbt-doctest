package com.github.tkawachi.doctest

import com.github.tkawachi.doctest.StringUtil.escape

/**
 * Test generator for µTest.
 */
object MicroTestGen extends TestGen {

  def generate(basename: String, pkg: Option[String], parsedList: Seq[ParsedDoctest]): String = {
    val pkgLine = pkg.fold("")(p => s"package $p")
    s"""$pkgLine
       |
       |import _root_.utest._
       |
       |object ${basename}Doctest extends TestSuite {
       |
       |    val tests = this {
       |
       |${StringUtil.indent(TestGen.helperMethods, "  ")}
       |
       |${parsedList.map(generateExample(basename, _)).mkString("\n\n")}
       |
       |    }
       |}
       |""".stripMargin
  }

  def generateExample(basename: String, parsed: ParsedDoctest): String = {
    s"""  "${escape(basename)}.scala:${parsed.lineNo}: ${parsed.symbol}"-{
       |${parsed.components.map(gen(parsed.lineNo, _)).mkString("\n\n")}
       |  }""".stripMargin
  }

  def gen(firstLine: Int, component: DoctestComponent): String =
    component match {
      case Example(expr, expected, _) =>
        val typeTest = expected.tpe.fold("")(tpe => genTypeTest(expr, tpe))
        s"""    "${componentDescription(component, firstLine)}"-{
                  val _actual_   = sbtDoctestReplString($expr)
                  val _expected_ = "${escape(expected.value)}"
           |      assert( _expected_ == _actual_ ) //$typeTest
           |    }""".stripMargin
      case Property(prop, _) =>
        s"""    "${componentDescription(component, firstLine)}"-{
           |      sbtDoctestReplString($prop)
           |    }""".stripMargin
      case Verbatim(code) =>
        StringUtil.indent(code, "    ")
    }

  def genTypeTest(expr: String, expectedType: String): String = {
    s"""
       |      sbtDoctestTypeEquals($expr)(($expr): $expectedType)""".stripMargin
  }

  def componentDescription(comp: DoctestComponent, firstLine: Int): String = {
    def absLine(lineNo: Int): Int = firstLine + lineNo - 1
    def mkStub(s: String): String = escape(StringUtil.truncate(s))

    comp match {
      case Example(expr, _, lineNo) =>
        s"example at line ${absLine(lineNo)}: ${mkStub(expr)}"
      case Property(prop, lineNo) =>
        s"property at line ${absLine(lineNo)}: ${mkStub(prop)}"
      case _ => ""
    }
  }
}
