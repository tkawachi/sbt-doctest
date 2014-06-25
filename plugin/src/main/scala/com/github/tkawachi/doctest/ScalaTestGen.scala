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
      |
      |class ${basename}Doctest extends FunSpec with Matchers with PropertyChecks {
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
      case e: Example =>
        s"""  it("${escapeDQ(basename)}.scala:${firstLine + e.line - 1}") {
         |    (${e.expr}).toString should equal("${escapeDQ(e.expected)}")
         |  }
         |""".stripMargin
      case p: Prop =>
        s"""  it("${escapeDQ(basename)}.scala:${firstLine + p.line - 1}") {
         |    forAll {
         |      ${p.prop}
         |    }
         |  }
         |""".stripMargin
      case i: Import =>
        i.importLine + "\n"
    }
  }

}
