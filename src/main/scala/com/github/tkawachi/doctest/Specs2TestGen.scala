package com.github.tkawachi.doctest

import java.io.File

import StringUtil.escape

/**
 * Test generator for specs2.
 */
object Specs2TestGen extends TestGen {
  override def generateBody(srcFile: File, basename: String, pkg: Option[String], examples: Seq[ParsedDoctest]): String = {
    val pkgLine = pkg.fold("")(p => s"package $p")
    s"""$pkgLine
       |
       |import org.scalacheck.Arbitrary._
       |import org.scalacheck.Prop._
       |
       |class ${basename}Doctest
       |    extends org.specs2.mutable.Specification
       |    with org.specs2.ScalaCheck {
       |
       |  ${StringUtil.indent(TestGen.helperMethods, "  ")}
       |
       |${examples.map(generateExample(basename, _)).mkString("\n\n")}
       |}
       |""".stripMargin
  }

  def generateExample(basename: String, parsed: ParsedDoctest): String = {
    s"""  ${generateLine(parsed.lineNo)}
       |  "${escape(basename)}.scala:${parsed.lineNo}: ${parsed.symbol}" should {
       |${parsed.components.map(gen(parsed.lineNo, _)).mkString("\n\n")}
       |  }""".stripMargin
  }

  def gen(firstLine: Int, component: DoctestComponent): String =
    component match {
      case Example(expr, expected, lineNo) =>
        val typeTest = expected.tpe.fold("")(tpe => genTypeTest(expr, tpe))
        s"""    ${generateLine(lineNo)}
           |    "${componentDescription(component, firstLine)}" in {$typeTest
           |      sbtDoctestReplString($expr) must_== "${escape(expected.value)}"
           |    }""".stripMargin
      case Property(prop, lineNo) =>
        s"""    ${generateLine(lineNo)}
           |    "${componentDescription(component, firstLine)}" ! prop {
           |      $prop
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
