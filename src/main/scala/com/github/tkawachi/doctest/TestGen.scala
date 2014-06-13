package com.github.tkawachi.doctest

trait TestGen {
  def generate(basename: String, pkg: Option[String], examples: Seq[ParsedDoctest]): String
}
