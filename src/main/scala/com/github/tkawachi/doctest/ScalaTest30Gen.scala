package com.github.tkawachi.doctest

/**
 * Test generator for ScalaTest < 3.1.0.
 */
class ScalaTest30Gen extends ScalaTestGen {
  override protected def withCheckersString: String = "with org.scalatest.prop.Checkers"
}
