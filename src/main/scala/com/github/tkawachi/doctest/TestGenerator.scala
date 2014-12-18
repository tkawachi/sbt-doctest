package com.github.tkawachi.doctest

import java.io.File
import DoctestPlugin.DoctestTestFramework
import scala.io.Source
import org.apache.commons.io.FilenameUtils

object TestGenerator {
  case class Result(pkg: Option[String], basename: String, testSource: String)

  val extractor = new Extractor

  private def testGen(framework: DoctestTestFramework): TestGen = framework match {
    case DoctestTestFramework.ScalaTest => ScalaTestGen
    case DoctestTestFramework.Specs2 => Specs2TestGen
    case DoctestTestFramework.ScalaCheck => ScalaCheckGen
  }

  /**
   * Generates test source code from scala source file.
   */
  def apply(srcFile: File, srcEncoding: String, framework: DoctestTestFramework): Seq[Result] = {
    val src = Source.fromFile(srcFile, srcEncoding).mkString
    val basename = FilenameUtils.getBaseName(srcFile.getName)
    extractor.extract(src)
      .flatMap(comment => CommentParser(comment).right.toOption.filter(_.components.size > 0))
      .groupBy(_.pkg).map {
        case (pkg, examples) =>
          Result(pkg, basename, testGen(framework).generate(basename, pkg, examples))
      }.toSeq
  }

  def findEncoding(scalacOptions: Seq[String]): Option[String] = {
    val index = scalacOptions.indexOf("-encoding")
    // The next element of "-encoding"
    if (index < 0 || index + 1 > scalacOptions.size - 1) None else Some(scalacOptions(index + 1))
  }
}
