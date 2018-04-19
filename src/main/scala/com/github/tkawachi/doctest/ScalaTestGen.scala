package com.github.tkawachi.doctest

import StringUtil.escape
import com.github.tkawachi.doctest.TestGen.containsProperty
import sbt.Keys.{ Classpath, moduleID }
import sbt.ModuleID

import scala.util.Try

/**
 * Test generator for ScalaTest.
 */
trait ScalaTestGen extends TestGen {

  private val st = "_root_.org.scalatest"

  def generate(basename: String, pkg: Option[String], parsedList: Seq[ParsedDoctest]): String = {
    val pkgLine = pkg.fold("")(p => s"package $p")
    s"""$pkgLine
       |
       |${TestGen.importArbitrary(parsedList)}
       |
       |class ${basename}Doctest
       |    extends $st.FunSpec
       |    with $st.Matchers
       |    ${withCheckers(parsedList)} {
       |
       |${StringUtil.indent(TestGen.helperMethods, "  ")}
       |
       |${parsedList.map(generateExample(basename, _)).mkString("\n\n")}
       |}
       |""".stripMargin
  }

  private def generateExample(basename: String, parsed: ParsedDoctest): String = {
    s"""  describe("${escape(basename)}.scala:${parsed.lineNo}: ${escape(parsed.symbol)}") {
       |${parsed.components.map(gen(parsed.lineNo, _)).mkString("\n\n")}
       |  }""".stripMargin
  }

  private def gen(firstLine: Int, component: DoctestComponent): String =
    component match {
      case Example(expr, expected, _) =>
        val typeTest = expected.tpe.fold("")(tpe => genTypeTest(expr, tpe))
        s"""    it("${componentDescription(component, firstLine)}") {
           |      sbtDoctestReplString($expr) should equal("${escape(expected.value)}")$typeTest
           |    }""".stripMargin
      case Property(prop, _) =>
        s"""    it("${componentDescription(component, firstLine)}") {
           |      check {
           |        $prop
           |      }
           |    }""".stripMargin
      case Verbatim(code) =>
        StringUtil.indent(code, "    ")
    }

  private def genTypeTest(expr: String, expectedType: String): String = {
    s"""
       |      sbtDoctestTypeEquals($expr)(($expr): $expectedType)""".stripMargin
  }

  private def componentDescription(comp: DoctestComponent, firstLine: Int): String = {
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

  protected def withCheckersString: String

  private def withCheckers(examples: Seq[ParsedDoctest]): String =
    if (containsProperty(examples)) withCheckersString else ""
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
