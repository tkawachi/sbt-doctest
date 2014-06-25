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

  def extract(scalaSource: String): Array[ScaladocComment] = parser.docDefs(scalaSource).map(toComment).toArray

  def extractBytes(scalaSource: String): Array[Byte] = {
    val baos = new java.io.ByteArrayOutputStream()
    val oos = new java.io.ObjectOutputStream(baos)
    oos.writeObject(extract(scalaSource))
    baos.toByteArray
  }

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
    new ScaladocComment(extractPkg(parsed).getOrElse(""), parsed.docDef.comment.raw, parsed.docDef.comment.pos.line)

}
