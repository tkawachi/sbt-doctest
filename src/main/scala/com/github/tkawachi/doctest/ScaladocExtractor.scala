package com.github.tkawachi.doctest

import scala.tools.nsc.doc.{ DocParser, Settings }
import java.io.File

/**
 * Extract examples from scala source.
 */
class ScaladocExtractor {

  private val settings = new Settings(Console println _)
  settings.bootclasspath.value = ScalaPath.pathList.mkString(File.pathSeparator)
  private val parser = new DocParser(settings)

  def extract(scalaSource: String): List[ScaladocComment] =
    parser.docDefs(scalaSource).map(toComment)

  private[this] def toComment(parsed: DocParser.Parsed) =
    ScaladocComment(extractPkg(parsed), parsed.nameChain.lastOption.map(_.decode).getOrElse(""),
      parsed.docDef.comment.raw, parsed.docDef.comment.pos.line)

  private[this] def extractPkg(parsed: DocParser.Parsed): Option[String] = {
    val packages = parsed.enclosing
      .collect { case pkgDef: parser.PackageDef => pkgDef.pid.toString() }
      .filter(_ != "<empty>")
    packages match {
      case Nil => None
      case lst => Some(lst.mkString("."))
    }
  }
}
