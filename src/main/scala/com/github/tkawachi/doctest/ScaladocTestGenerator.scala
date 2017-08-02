package com.github.tkawachi.doctest

import java.io.File
import scala.io.Source
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.StringEscapeUtils.unescapeHtml4

object ScaladocTestGenerator {

  val extractor = new ScaladocExtractor

  private def decodeHtml(comment: ScaladocComment) = comment.copy(text = unescapeHtml4(comment.text))

  /**
   * Generates test source code from scala source file.
   */
  def apply(srcFile: File, srcEncoding: String, testGen: TestGen, decodeHtmlEnabled: Boolean): Seq[TestSource] = {
    val src = Source.fromFile(srcFile, srcEncoding).mkString
    val basename = FilenameUtils.getBaseName(srcFile.getName)
    extractor.extract(src)
      .map { comment =>
        if (decodeHtmlEnabled) decodeHtml(comment)
        else comment
      }
      .flatMap(comment => CommentParser(comment).right.toOption.filter(_.components.nonEmpty))
      .groupBy(_.pkg).map {
        case (pkg, examples) =>
          TestSource(pkg, basename, testGen.generate(basename, pkg, examples))
      }
      .toSeq
  }

  def findEncoding(scalacOptions: Seq[String]): Option[String] = {
    val index = scalacOptions.indexOf("-encoding")
    // The next element of "-encoding"
    if (index < 0 || index + 1 > scalacOptions.size - 1) None else Some(scalacOptions(index + 1))
  }
}
