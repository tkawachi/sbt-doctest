package com.github.tkawachi.doctest

import scala.meta._
import scala.meta.contrib._

/**
 * Extract examples from scala source.
 */
object ScaladocExtractor {

  private val meaningfulDocTokenKinds: Set[DocToken.Kind] =
    Set(DocToken.CodeBlock, DocToken.Description, DocToken.Example)

  def extract(scalaSource: String): List[ScaladocComment] = {

    val code = scalaSource.parse[Source].get
    val comments = AssociatedComments(code)

    object NamedMember {
      def unapply(t: Tree): Option[String] = t match {
        case m: Member => Some(m.name.value)
        case v: Defn.Val => v.pats.collectFirst { case m: Member => m.name.value }
        case v: Defn.Var => v.pats.collectFirst { case m: Member => m.name.value }
        case _ => None
      }
    }

    def pkgOf(t: Tree): Option[String] =
      t.ancestors.collect { case pkg: Pkg => pkg.ref.toString } match {
        case Nil => None
        case names => Some(names.mkString("."))
      }

    def parsedScalaDocComment(t: Tree): Option[ScaladocComment] =
      (t, comments.leading(t).filter(_.isScaladoc).toList) match {
        // take only named members having single scaladoc comment
        case (NamedMember(name), List(scalaDocComment)) if name.nonEmpty =>
          scalaDocComment.docTokens.flatMap(
            _.filter(dt => meaningfulDocTokenKinds(dt.kind)) match {
              case Nil => None
              case docTokens =>
                Some(
                  ScaladocComment(
                    pkg = pkgOf(t),
                    symbol = name,
                    codeBlocks = docTokens.collect {
                      case DocToken(DocToken.CodeBlock, _, Some(body)) if body.trim.nonEmpty => body
                    },
                    text = scalaDocComment.syntax,
                    lineNo = scalaDocComment.pos.startLine + 1 //startLine is 0 based, so compensating here
                  ))
            })
        case _ => None
      }

    def extractAllCommentsFrom(t: Tree): List[ScaladocComment] =
      t.children.foldLeft(parsedScalaDocComment(t).toList) {
        case (extractedSoFar, childTree) => extractedSoFar ::: extractAllCommentsFrom(childTree)
      }

    extractAllCommentsFrom(code)
  }
}
