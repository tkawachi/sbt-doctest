package com.github.tkawachi.doctest

import java.io.File

import com.github.tkawachi.doctest.StringUtil.escape

object ScalaCheckGen extends TestGen {

  override def generateBody(srcFile: File, basename: String, pkg: Option[String], parsedList: Seq[ParsedDoctest]): String = {
    val pkgLine = pkg.fold("")(p => s"package $p")
    s"""$pkgLine
       |
       |import org.scalacheck.Arbitrary._
       |import org.scalacheck.Prop._
       |
       |object ${basename}Doctest
       |    extends org.scalacheck.Properties("${escape(basename)}.scala") {
       |
       |  ${StringUtil.indent(TestGen.helperMethods, "  ")}
       |
       |${parsedList.map(generateExample(basename, _)).mkString("\n\n")}
       |}
       |""".stripMargin
  }

  def generateExample(basename: String, parsed: ParsedDoctest): String = {
    s"""  ${generateLine(parsed.lineNo)}
       |  include(new org.scalacheck.Properties("${parsed.symbol}") {
       |${parsed.components.map(gen(parsed.lineNo, _)).mkString("\n\n")}
       |  })""".stripMargin
  }

  def gen(firstLine: Int, component: DoctestComponent): String =
    component match {
      case Example(expr, expected, lineNo) =>
        val typeTest = expected.tpe.fold("")(tpe => genTypeTest(expr, tpe))
        s"""    ${generateLine(lineNo)}
           |    property("${componentDescription(component, firstLine)}") = org.scalacheck.Prop.secure {
           |      ${typeTest}val actual = sbtDoctestReplString($expr)
           |      val expected = "${escape(expected.value)}"
           |      (actual == expected) :| s"'$$actual' is not equal to '$$expected'"
           |    }""".stripMargin
      case Property(prop, lineNo) =>
        s"""    ${generateLine(lineNo)}
           |    property("${componentDescription(component, firstLine)}") = org.scalacheck.Prop.forAll {
           |      $prop
           |    }""".stripMargin
      case Verbatim(code, lineNo) =>
        StringUtil.indent(generateLine(lineNo) + "\n" + code, "    ")
    }

  def genTypeTest(expr: String, expectedType: String): String =
    s"      sbtDoctestTypeEquals($expr)(($expr): $expectedType)\n"

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
