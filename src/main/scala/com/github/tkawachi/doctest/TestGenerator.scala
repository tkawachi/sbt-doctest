package com.github.tkawachi.doctest

import java.io.File
import scala.io.Source
import org.apache.commons.io.FilenameUtils

object TestGenerator {
  case class Result(pkg: Option[String], basename: String, testSource: String)

  val extractor = new Extractor

  /**
   * Generates test source code from scala source file.
   */
  def apply(srcFile: File): Seq[Result] = {
    val src = Source.fromFile(srcFile).mkString
    val basename = FilenameUtils.getBaseName(srcFile.getName)
    extractor.extract(src)
      .flatMap(comment => CommentParser(comment).right.toOption.filter(_.components.size > 0))
      .groupBy(_.pkg).map {
        case (pkg, examples) =>
          Result(pkg, basename, ScalaTestGen.generate(basename, pkg, examples))
      }.toSeq
  }
}
