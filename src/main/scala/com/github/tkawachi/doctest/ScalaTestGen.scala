package com.github.tkawachi.doctest

import StringUtil.{ escapeDoubleQuote => escapeDQ }

object ScalaTestGen extends TestGen {

  def generate(basename: String, pkg: Option[String], parsedList: Seq[ParsedDoctest]): String = {
    val pkgLine = pkg.fold("")(p => s"package $p")
    s"""$pkgLine
      |
      |import org.scalatest.{ Matchers, FunSpec }
      |import org.scalatest.prop.PropertyChecks
      |import org.scalacheck.Arbitrary._
      |import org.scalacheck.Prop._
      |import scala.reflect.runtime.universe._
      |
      |class ${basename}Doctest extends FunSpec with Matchers with PropertyChecks {
      |
      |  def getType[A: TypeTag](a: A) = typeOf[A]
      |
      |${parsedList.map(generateIt(basename, _)).mkString}
      |}
      |""".stripMargin
  }

  def generateIt(basename: String, parsed: ParsedDoctest): String = {
    s"""describe("${escapeDQ(basename)}.scala:${parsed.lineno}") {
       |${parsed.components.map(gen(basename, parsed.lineno, _)).mkString}
       |}
       |""".stripMargin
  }

  def gen(basename: String, firstLine: Int, component: DoctestComponent): String = {
    component match {
      case Example(expr, expected, lineno) =>
        val typeTest = expected.tpe.fold("")(tpe => "\n    " + genTypeTest(expr, tpe))
        s"""  it("${escapeDQ(basename)}.scala:${firstLine + lineno - 1}") {
         |    ($expr).toString should equal("${escapeDQ(expected.value)}")$typeTest
         |  }
         |""".stripMargin
      case Prop(prop, lineno) =>
        s"""  it("${escapeDQ(basename)}.scala:${firstLine + lineno - 1}") {
         |    forAll {
         |      $prop
         |    }
         |  }
         |""".stripMargin
      case Import(line) =>
        line + "\n"
    }
  }

  def genTypeTest(expr: String, tpe: String): String =
    s"""require(typeOf[$tpe] =:= getType($expr), "$tpe != " + getType($expr).toString)"""

}
