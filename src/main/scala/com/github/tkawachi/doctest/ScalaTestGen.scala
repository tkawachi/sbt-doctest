package com.github.tkawachi.doctest

import StringUtil.{ escapeDoubleQuote => escapeDQ }

object ScalaTestGen extends TestGen {
  private val st = "org.scalatest"
  private val ru = "scala.reflect.runtime.universe"

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
       |  def sbtDoctestGetType[A: $ru.TypeTag](a: A): $ru.Type =
       |    $ru.typeOf[A]
       |
       |${parsedList.map(generateIt(basename, _)).mkString("\n\n")}
       |}
       |""".stripMargin
  }

  def generateIt(basename: String, parsed: ParsedDoctest): String = {
    s"""  describe("${escapeDQ(basename)}.scala:${parsed.lineno}: ${parsed.symbol}") {
       |${parsed.components.map(gen(basename, parsed.lineno, _)).mkString("\n\n")}
       |  }""".stripMargin
  }

  def gen(basename: String, firstLine: Int, component: DoctestComponent): String = {
    component match {
      case Example(expr, expected, lineNo) =>
        val typeTest = expected.tpe.fold("")(tpe => genTypeTest(expr, tpe))
        s"""    it("${escapeDQ(basename)}.scala:${firstLine + lineNo - 1}") {
           |      ($expr).toString should equal("${escapeDQ(expected.value)}")$typeTest
           |    }""".stripMargin
      case Property(prop, lineNo) =>
        s"""    it("${escapeDQ(basename)}.scala:${firstLine + lineNo - 1}") {
           |      forAll {
           |        $prop
           |      }
           |    }""".stripMargin
      case Verbatim(code) =>
        s"    $code"
    }
  }

  def genTypeTest(expr: String, expectedType: String): String = {
    s"""
       |      val sbtDoctestExpectedType = $ru.typeOf[$expectedType]
       |      val sbtDoctestActualType = sbtDoctestGetType($expr)
       |      require(sbtDoctestExpectedType =:= sbtDoctestActualType,
       |        "$expectedType != " + sbtDoctestActualType.toString)""".stripMargin
  }

}
