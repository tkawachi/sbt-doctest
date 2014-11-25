package com.github.tkawachi.doctest

import java.io.File

/**
 * Interface of a test generator.
 */
trait TestGen {
  def generate(srcFile: File, basename: String, pkg: Option[String], examples: Seq[ParsedDoctest]): String = {
    generateHeader(srcFile) + generateBody(srcFile, basename, pkg, examples)
  }

  private def generateHeader(srcFile: File): String =
    GeneratedSource.MARKER + "\n" +
      GeneratedSource.SOURCE_PREFIX + srcFile.getAbsolutePath + "\n"

  protected def generateLine(lineNo: Int): String =
    GeneratedSource.LINE_PREFIX + lineNo.toString

  protected def generateBody(srcFile: File, basename: String, pkg: Option[String], examples: Seq[ParsedDoctest]): String
}

object TestGen {
  /**
   * Helper methods which will be embedded in generated tests.
   */
  val helperMethods =
    """def sbtDoctestTypeEquals[A](a1: => A)(a2: => A) = ()
      |def sbtDoctestReplString(any: Any): String = {
      |  val s = scala.runtime.ScalaRunTime.replStringOf(any, 1000).init
      |  if (s.headOption == Some('\n')) s.tail else s
      |}
    """.stripMargin
}
