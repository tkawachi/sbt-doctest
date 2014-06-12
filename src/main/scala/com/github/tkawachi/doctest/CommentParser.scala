package com.github.tkawachi.doctest

import scala.util.parsing.combinator.RegexParsers
import scala.util.parsing.input.Positional

object CommentParser extends RegexParsers {
  case class Extracted(expr: String, example: String, line: Int)
  case class PositionedString(s: String) extends Positional

  def eol = opt('\r') <~ '\n'
  def anyLine = ".*".r <~ eol ^^ (_ => None)
  def lines = rep(example | anyLine) <~ ".*".r ^^ (_.flatten)
  def leadingChars = "[/\\* \t]".r
  def exprPrompt = rep(leadingChars) <~ ">>> " ^^ { _.size }
  def strRep1 = positioned(".+".r ^^ { PositionedString })
  def exprLine = exprPrompt ~ strRep1 <~ eol
  def exampleLine = rep(leadingChars) ~ ".+".r <~ eol
  def example = exprLine ~ exampleLine ^^ {
    case (exprLeadingLen ~ expr) ~ (exampleLeading ~ exampleRest) =>
      if (exampleLeading.size < exprLeadingLen) {
        None
      } else {
        val ex = exampleLeading.drop(exprLeadingLen).mkString + exampleRest
        Some(Extracted(expr.s, ex, expr.pos.line))
      }
  }
  def parse(input: String) = parseAll(lines, input)

  def apply(input: String): Either[String, List[Extracted]] with Product with Serializable = parse(input) match {
    case Success(examples, _) => Right(examples)
    case NoSuccess(msg, next) => Left(s"$msg on line ${next.pos.line}, column ${next.pos.column}")
  }

  override def skipWhitespace = false

}
