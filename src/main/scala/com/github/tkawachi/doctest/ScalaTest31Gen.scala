package com.github.tkawachi.doctest

/**
 * Test generator for ScalaTest >= 3.1.0.
 */
object ScalaTest31Gen extends ScalaTestGen {
  override protected def withCheckersString: String = "with _root_.org.scalatest.check.Checkers"
}
