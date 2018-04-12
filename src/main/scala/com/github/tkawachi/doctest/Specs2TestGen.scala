package com.github.tkawachi.doctest

/**
 * Test generator for specs2.
 */
object Specs2TestGen extends TestGen {

  override protected def importsLine(parsedList: Seq[ParsedDoctest]): String =
    TestGen.importArbitrary(parsedList)

  override protected def suiteDeclarationLine(basename: String, parsedList: Seq[ParsedDoctest]): String = {
    s"""class ${basename}Doctest
       |    extends _root_.org.specs2.mutable.Specification
       |    with _root_.org.specs2.ScalaCheck""".stripMargin
  }

  override protected def generateTestCase(caseName: String, caseBody: String): String =
    s"""  "$caseName" should {
       |$caseBody
       |  }""".stripMargin

  override protected def generateExample(description: String, assertions: String): String =
    s"""    "$description" in {
       |      $assertions
       |    }""".stripMargin

  override protected def generatePropertyExample(description: String, property: String): String =
    s"""    "$description" ! prop {
       |      $property
       |    }""".stripMargin

  override protected def generateAssert(actual: String, expected: String): String =
    s"""$actual must_== "$expected""""
}
