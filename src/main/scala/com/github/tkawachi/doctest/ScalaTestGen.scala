package com.github.tkawachi.doctest

import com.github.tkawachi.doctest.TestGen.containsProperty
import sbt.Keys.{ Classpath, moduleID }
import sbt.ModuleID

import scala.util.Try

/**
 * Test generator for ScalaTest.
 */
trait ScalaTestGen extends TestGen {

  override protected def importsLine(parsedList: Seq[ParsedDoctest]): String =
    TestGen.importArbitrary(parsedList)

  override protected def suiteDeclarationLine(basename: String, parsedList: Seq[ParsedDoctest]): String = {
    val withCheckers: String = if (containsProperty(parsedList)) withCheckersString else ""

    val st = "_root_.org.scalatest"
    s"""class ${basename}Doctest
       |    extends $st.FunSpec
       |    with $st.Matchers
       |    $withCheckers""".stripMargin
  }

  override protected def generateTestCase(caseName: String, caseBody: String): String = {
    s"""  describe("$caseName") {
       |$caseBody
       |  }""".stripMargin
  }

  override protected def generateExample(description: String, assertions: String): String =
    s"""    it("$description") {
       |      $assertions
       |    }""".stripMargin

  override protected def generatePropertyExample(description: String, property: String): String =
    s"""    it("$description") {
       |      check {
       |        $property
       |      }
       |    }""".stripMargin

  override protected def generateAssert(actual: String, expected: String): String =
    s"""$actual should equal("$expected")"""

  protected def withCheckersString: String

}

object ScalaTestGen {

  def hasGreaterThanOrEqualTo310(testClasspath: Classpath, scalaVersion: String): Boolean = {
    val scalaBinVersion = scalaVersion.split('.').take(2).mkString(".")
    testClasspath.exists { entry =>
      val maybeModuleId = entry.get(moduleID.key)
      maybeModuleId.exists(mod => isGreaterThanOrEqualTo310(mod, scalaBinVersion))
    }
  }

  private def isGreaterThanOrEqualTo310(moduleID: ModuleID, scalaBinVersion: String): Boolean = {
    moduleID.organization == "org.scalatest" &&
      moduleID.name == s"scalatest_$scalaBinVersion" &&
      (moduleID.revision.split('.') match {
        case Array(majorString, minorString, _*) =>
          (for {
            major <- Try(majorString.toInt)
            minor <- Try(minorString.toInt)
          } yield major >= 3 && minor >= 1).getOrElse(false)
        case _ => false
      })
  }
}
