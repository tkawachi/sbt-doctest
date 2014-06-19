package com.github.tkawachi.doctest

object TestGenerator {
  case class Result(pkg: String, basename: String, testSource: String)

  def apply(basename: String, parsedDocTests: Seq[ParsedDoctest]): Seq[Result] = {
    parsedDocTests.groupBy(_.pkg).map {
      case (pkg, examples) =>
        Result(pkg, basename, ScalaTestGen.generate(basename, Option(pkg), examples))
    }.toSeq
  }
}
