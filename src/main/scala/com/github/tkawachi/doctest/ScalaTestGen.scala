package com.github.tkawachi.doctest

object ScalaTestGen extends TestGen {

  def generate(basename: String, pkg: Option[String], examples: Seq[Example]): String = {
    val pkgLine = pkg.fold("")(p => s"package $p")
    s"""$pkgLine
      |
      |import org.scalatest.{ Matchers, FunSpec }
      |
      |class ${basename}Doctest extends FunSpec with Matchers {
      |${examples.map(generateIt(basename, _)).mkString}
      |}
      |""".stripMargin
  }

  def generateIt(basename: String, example: Example): String =
    s"""  it("$basename.scala:${example.lineno}") {
       |    (${example.expr}).toString should equal(\"\"\"${example.expected}\"\"\")
       |  }
       |""".stripMargin

}
