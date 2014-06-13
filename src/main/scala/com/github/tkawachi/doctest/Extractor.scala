package com.github.tkawachi.doctest

import com.github.tkawachi.doctest.CommentParser.{ ExtractedProp, Extracted }

import scala.tools.nsc.doc.{ DocParser, Settings }
import java.io.File

/**
 * Extract examples from scala source.
 */
class Extractor {
  private val settings = new Settings(Console println _)
  settings.bootclasspath.value = ScalaPath.pathList.mkString(File.pathSeparator)
  private val parser = new DocParser(settings)

  def extract(scalaSource: String): List[ParsedDoctest] = parser.docDefs(scalaSource).map(extract).flatten

  private[this] def extractPkg(parsed: DocParser.Parsed): Option[String] = {
    parsed.enclosing
      .filter(_.isInstanceOf[parser.PackageDef])
      .map(_.asInstanceOf[parser.PackageDef].pid.toString())
      .filter(_ != "<empty>") match {
        case Nil => None
        case lst => Some(lst.mkString("."))
      }
  }

  private[this] def extract(parsed: DocParser.Parsed): Option[ParsedDoctest] =
    extractFromComment(extractPkg(parsed), parsed.docDef.comment.raw, parsed.docDef.comment.pos.line)

  private[doctest] def extractFromComment(pkg: Option[String], comment: String, firstLine: Int): Option[ParsedDoctest] = {
    CommentParser(comment) match {
      case Right(Nil) => None
      case Right(list) => Some(ParsedDoctest(pkg, list, firstLine))
      case Left(msg) =>
        println(msg)
        None
    }
  }
}
