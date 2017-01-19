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

  def importArbitrary(examples: Seq[ParsedDoctest]): String =
    if (containsProperty(examples)) "import org.scalacheck.Arbitrary._" else ""

  def withCheckers(examples: Seq[ParsedDoctest]): String =
    if (containsProperty(examples)) "with org.scalatest.prop.Checkers" else ""

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
