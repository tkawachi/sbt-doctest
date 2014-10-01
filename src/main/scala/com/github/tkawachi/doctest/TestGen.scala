package com.github.tkawachi.doctest

/**
 * Interface of a test generator.
 */
trait TestGen {
  def generate(basename: String, pkg: Option[String], examples: Seq[ParsedDoctest]): String
}
