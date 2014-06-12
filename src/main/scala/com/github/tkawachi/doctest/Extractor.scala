package com.github.tkawachi.doctest

import com.github.tkawachi.doctest.CommentParser.Extracted

import scala.tools.nsc.doc.{ DocParser, Settings }
import java.io.File

/**
 * Extract examples from scala source.
 */
class Extractor {
  private val settings = new Settings(Console println _)
  settings.bootclasspath.value = ScalaPath.pathList.mkString(File.pathSeparator)
  private val parser = new DocParser(settings)

  def extract(scalaSource: String): List[Example] = parser.docDefs(scalaSource).flatMap(extract)

  private[this] def extractPkg(parsed: DocParser.Parsed): Option[String] = {
    parsed.enclosing
      .filter(_.isInstanceOf[parser.PackageDef])
      .map(_.asInstanceOf[parser.PackageDef].pid.toString())
      .filter(_ != "<empty>") match {
        case Nil => None
        case lst => Some(lst.mkString("."))
      }
  }

  private[this] def extract(parsed: DocParser.Parsed): Seq[Example] =
    extractFromComment(extractPkg(parsed), parsed.docDef.comment.raw, parsed.docDef.comment.pos.line)

  private[doctest] def extractFromComment(pkg: Option[String], comment: String, firstLine: Int): Seq[Example] = {
    CommentParser(comment) match {
      case Right(list) =>
        list.map { case Extracted(expr, example, line) => Example(pkg, expr, example, firstLine + line - 1) }
      case Left(msg) =>
        println(msg)
        Seq()
    }

  }
}
