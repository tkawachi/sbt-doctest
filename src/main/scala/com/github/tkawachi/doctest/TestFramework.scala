package com.github.tkawachi.doctest

sealed abstract class TestFramework
case object Specs2 extends TestFramework
case object ScalaTest extends TestFramework
case object ScalaCheck extends TestFramework

object TestFramework {
  /**
   * Get TestFramework from a string.
   * It's case insensitive.
   *
   * {{{
   * >>> import TestFramework.get
   * >>> get("scalatest")
   * Some(ScalaTest)
   *
   * >>> get("ScalaTest")
   * Some(ScalaTest)
   *
   * >>> get("specs2")
   * Some(Specs2)
   *
   * >>> get("scalacheck")
   * Some(ScalaCheck)
   *
   * >>> get("foobar")
   * None
   * }}}
   */
  def get(s: String): Option[TestFramework] = s.toLowerCase match {
    case "scalatest" => Some(ScalaTest)
    case "specs2" => Some(Specs2)
    case "scalacheck" => Some(ScalaCheck)
    case _ => None
  }

  def apply(s: String): TestFramework =
    get(s).getOrElse(sys.error(s"Unknown framework: $s. Sepcify scalatest or specs2."))
}
