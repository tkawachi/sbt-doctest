package com.github.tkawachi.doctest

import StringUtil.{ escapeDoubleQuote => escapeDQ }

/**
 * Test generator for ScalaTest.
 */
object ScalaTestGen extends TestGen {
  private val st = "org.scalatest"

  def generate(basename: String, pkg: Option[String], parsedList: Seq[ParsedDoctest]): String = {
    val pkgLine = pkg.fold("")(p => s"package $p")
    s"""$pkgLine
       |
       |import org.scalacheck.Arbitrary._
       |import org.scalacheck.Prop._
       |
       |class ${basename}Doctest
       |    extends $st.FunSpec
       |    with $st.Matchers
       |    with $st.prop.PropertyChecks {
       |
       |  def sbtDoctestTypeEquals[A, B](a: => A, b: => B)(implicit ev: A =:= B): Boolean = true
       |
       |${parsedList.map(generateExample(basename, _)).mkString("\n\n")}
       |}
       |""".stripMargin
  }

  def generateExample(basename: String, parsed: ParsedDoctest): String = {
    s"""  describe("${escapeDQ(basename)}.scala:${parsed.lineNo}: ${parsed.symbol}") {
       |${parsed.components.map(gen(parsed.lineNo, _)).mkString("\n\n")}
       |  }""".stripMargin
  }

  def gen(firstLine: Int, component: DoctestComponent): String =
    component match {
      case Example(expr, expected, _) =>
        val typeTest = expected.tpe.fold("")(tpe => genTypeTest(expr, tpe))
        s"""    it("${componentDescription(component, firstLine)}") {
           |      ($expr).toString should equal("${escapeDQ(expected.value)}")$typeTest
           |    }""".stripMargin
      case Property(prop, _) =>
        s"""    it("${componentDescription(component, firstLine)}") {
           |      forAll {
           |        $prop
           |      }
           |    }""".stripMargin
      case Verbatim(code) =>
        StringUtil.indent(code, "    ")
    }

  def genTypeTest(expr: String, expectedType: String): String = {
    s"""
       |      sbtDoctestTypeEquals($expr, ($expr): $expectedType)""".stripMargin
  }

  def componentDescription(comp: DoctestComponent, firstLine: Int): String = {
    def absLine(lineNo: Int): Int = firstLine + lineNo - 1
    def mkStub(s: String): String = escapeDQ(StringUtil.truncate(s))

    comp match {
      case Example(expr, _, lineNo) =>
        s"example at line ${absLine(lineNo)}: ${mkStub(expr)}"
      case Property(prop, lineNo) =>
        s"property at line ${absLine(lineNo)}: ${mkStub(prop)}"
      case _ => ""
    }
  }
}
