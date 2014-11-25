package com.github.tkawachi.doctest

import java.io.File

import StringUtil.escape

/**
 * Test generator for ScalaTest.
 */
object ScalaTestGen extends TestGen {
  private val st = "org.scalatest"

  override def generateBody(srcFile: File, basename: String, pkg: Option[String], parsedList: Seq[ParsedDoctest]): String = {
    val pkgLine = pkg.fold("")(p => s"package $p")
    s"""$pkgLine
       |
       |import org.scalacheck.Arbitrary._
       |import org.scalacheck.Prop._
       |
       |class ${basename}Doctest
       |    extends $st.FunSpec
       |    with $st.Matchers
       |    with $st.prop.Checkers {
       |
       |  ${StringUtil.indent(TestGen.helperMethods, "  ")}
       |
       |${parsedList.map(generateExample(basename, _)).mkString("\n\n")}
       |}
       |""".stripMargin
  }

  def generateExample(basename: String, parsed: ParsedDoctest): String = {
    s"""  ${generateLine(parsed.lineNo)}
       |  describe("${escape(basename)}.scala:${parsed.lineNo}: ${parsed.symbol}") {
       |${parsed.components.map(gen(parsed.lineNo, _)).mkString("\n\n")}
       |  }""".stripMargin
  }

  def gen(firstLine: Int, component: DoctestComponent): String =
    component match {
      case Example(expr, expected, lineNo) =>
        val typeTest = expected.tpe.fold("")(tpe => genTypeTest(expr, tpe))
        s"""    ${generateLine(lineNo)}
           |    it("${componentDescription(component, firstLine)}") {
           |      sbtDoctestReplString($expr) should equal("${escape(expected.value)}")$typeTest
           |    }""".stripMargin
      case Property(prop, lineNo) =>
        s"""    ${generateLine(lineNo)}
           |    it("${componentDescription(component, firstLine)}") {
           |      check {
           |        $prop
           |      }
           |    }""".stripMargin
      case Verbatim(code, lineNo) =>
        StringUtil.indent(generateLine(lineNo) + "\n" + code, "    ")
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
