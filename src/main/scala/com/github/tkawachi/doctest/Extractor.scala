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

  def extract(scalaSource: String): List[Example] = parser.docDefs(scalaSource).flatMap(extract)

  private[this] val reExpr = "^([/\\*\\s]*)>>> (.+)$".r

  private[this] def extractPkg(parsed: DocParser.Parsed): Option[String] = {
    parsed.enclosing
      .filter(_.isInstanceOf[parser.PackageDef])
      .map(_.asInstanceOf[parser.PackageDef].pid.toString())
      .filter(_ != "<empty>") match {
        case Nil => None
        case lst => Some(lst.mkString("."))
      }
  }

  private[this] def extract(parsed: DocParser.Parsed): Seq[Example] = {
    val examples = Seq.newBuilder[Example]

    var expr: Option[(Int, Int, String)] = None

    val pkg = extractPkg(parsed)

    val comment = parsed.docDef.comment
    parsed.docDef.comment.raw.lines.zip(Iterator.from(comment.pos.line)).foreach {
      case (line, lineno) =>
        expr match {
          case None =>
            line match {
              case reExpr(indent, e) => expr = Some(lineno, indent.size, e)
              case _ =>
            }
          case Some(e) =>
            if (line.size >= e._2) {
              examples += Example(pkg, e._3, line.substring(e._2), e._1)
            }
            expr = None
        }
    }

    examples.result()
  }
}
