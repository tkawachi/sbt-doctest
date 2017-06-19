package com.github.tkawachi.doctest

import java.io.File
import scala.io.Source
import com.github.tkawachi.doctest.DoctestPlugin.DoctestTestFramework

object MarkdownTestGenerator {

  val extractor = new MarkdownCodeblocksExtractor

  def apply(source: File, framework: DoctestTestFramework): Seq[TestSource] = {
    val contents = Source.fromFile(source).mkString
    val basename = StringUtil.getBaseName(source.getName)
    extractor.extract(contents)
      .flatMap(codeblock => CodeblockParser(codeblock).right.toOption.filter(_.components.size > 0))
      .groupBy(_.pkg).map {
        case (pkg, examples) => TestSource(pkg, basename, testGen(framework).generate(basename, pkg, examples))
      }
      .toSeq
  }

}
