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
    """def sbtDoctestTypeEquals[A](a1: => A)(a2: => A): _root_.scala.Unit = ()
      |def sbtDoctestReplString(any: _root_.scala.Any): _root_.scala.Predef.String = {
      |  val s = _root_.scala.runtime.ScalaRunTime.replStringOf(any, 1000).init
      |  if (s.headOption == Some('\n')) s.tail else s
      |}""".stripMargin

  def importArbitrary(examples: Seq[ParsedDoctest]): String =
    if (containsProperty(examples)) "import _root_.org.scalacheck.Arbitrary._" else ""

  def containsExample(examples: Seq[ParsedDoctest]): Boolean =
    examples.exists(_.components.exists {
      case _: Example => true
      case _ => false
    })

  def containsProperty(examples: Seq[ParsedDoctest]): Boolean =
    examples.exists(_.components.exists {
      case _: Property => true
      case _ => false
    })
}
