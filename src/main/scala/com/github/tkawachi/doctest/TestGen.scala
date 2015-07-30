package com.github.tkawachi.doctest

/**
 * Interface of a test generator.
 */
trait TestGen {
  def generate(basename: String, pkg: Option[String], examples: Seq[ParsedDoctest]): String
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
      |}""".stripMargin
}
