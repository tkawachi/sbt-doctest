package com.github.tkawachi.doctest

import java.io.File
import scala.io.Source
import org.apache.commons.io.FilenameUtils

object MarkdownTestGenerator {

  val extractor = new MarkdownCodeblocksExtractor

  def apply(source: File, testGen: TestGen): Seq[TestSource] = {
    val contents = Source.fromFile(source).mkString
    val basename = FilenameUtils.getBaseName(source.getName)
    extractor.extract(contents)
      .flatMap(codeblock => CodeblockParser(codeblock).right.toOption.filter(_.components.nonEmpty))
      .groupBy(_.pkg).map {
        case (pkg, examples) => TestSource(pkg, basename, testGen.generate(basename, pkg, examples))
      }
      .toSeq
  }

}
