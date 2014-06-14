package com.github.tkawachi.doctest

import scala.util.parsing.combinator.RegexParsers
import scala.util.parsing.input.Positional

object CommentParser extends RegexParsers {
  sealed abstract class DoctestComponent
  case class Extracted(expr: String, example: String, line: Int) extends DoctestComponent
  case class ExtractedProp(prop: String, line: Int) extends DoctestComponent
  case class Import(importLine: String) extends DoctestComponent

  case class PositionedString(s: String) extends Positional

  val PYTHON_STYLE_PROMPT = ">>> "
  val REPL_STYLE_PROMPT = "scala> "
  val PROP_PROMPT = "prop> "

  def eol = opt('\r') <~ '\n'
  def anyLine = ".*".r <~ eol ^^ (_ => None)
  def lines = rep(importLine | example | replExample | propLine | anyLine) <~ ".*".r ^^ (_.flatten)
  def leadingChar = ('/': Parser[Char]) | '*' | ' ' | '\t'
  def leadingString = rep(leadingChar) ^^ (_.mkString)
  def strRep1 = positioned(".+".r ^^ { PositionedString })
  def importLine = leadingString ~> (REPL_STYLE_PROMPT | PYTHON_STYLE_PROMPT) ~> "import\\s+\\S+".r <~ eol ^^ (s => Some(Import(s)))
  def exprPrompt = leadingString <~ PYTHON_STYLE_PROMPT
  def exprLine = exprPrompt ~ strRep1 <~ eol
  def exampleLine = leadingString ~ ".+".r <~ eol
  def example = exprLine ~ exampleLine ^^ {
    case (exprLeading ~ expr) ~ (exampleLeading ~ exampleRest) =>
      if (exampleLeading.startsWith(exprLeading)) {
        val ex = exampleLeading.drop(exprLeading.size) + exampleRest
        Some(Extracted(expr.s, ex, expr.pos.line))
      } else {
        None
      }
  }

  def replPrompt = leadingString <~ REPL_STYLE_PROMPT
  def replLine = replPrompt ~ strRep1 <~ eol
  def replResultLine = (leadingString <~ "res\\d+: \\w+ = ".r) ~ ".+".r <~ eol
  def replExample = replLine ~ replResultLine ^^ {
    case (exprLeading ~ expr) ~ (resultLeading ~ result) =>
      if (exprLeading == resultLeading) Some(Extracted(expr.s, result, expr.pos.line))
      else None
  }

  def propPrompt = leadingString <~ PROP_PROMPT
  def propLine = propPrompt ~> strRep1 <~ eol ^^ (ps => Some(ExtractedProp(ps.s, ps.pos.line)))

  def parse(input: String) = parseAll(lines, input)

  def apply(input: String): Either[String, List[DoctestComponent]] with Product with Serializable = parse(input) match {
    case Success(examples, _) => Right(examples)
    case NoSuccess(msg, next) => Left(s"$msg on line ${next.pos.line}, column ${next.pos.column}")
  }

  override def skipWhitespace = false

}
