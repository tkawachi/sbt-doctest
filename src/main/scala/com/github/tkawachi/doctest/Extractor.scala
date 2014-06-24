package com.github.tkawachi.doctest

import scala.tools.nsc.doc.{ DocParser, Settings }
import java.io.File

/**
 * Extract examples from scala source.
 */
class Extractor {

  private val settings = new Settings(Console println _)
  settings.bootclasspath.value = ScalaPath.pathList.mkString(File.pathSeparator)
  private val parser = new DocParser(settings)

  def extract(scalaSource: String): List[ScaladocComment] = parser.docDefs(scalaSource).map(toComment)

  private[this] def extractPkg(parsed: DocParser.Parsed): Option[String] = {
    parsed.enclosing
      .filter(_.isInstanceOf[parser.PackageDef])
      .map(_.asInstanceOf[parser.PackageDef].pid.toString())
      .filter(_ != "<empty>") match {
        case Nil => None
        case lst => Some(lst.mkString("."))
      }
  }

  private[this] def toComment(parsed: DocParser.Parsed) =
    ScaladocComment(extractPkg(parsed), parsed.docDef.comment.raw, parsed.docDef.comment.pos.line)

}
