package com.github.tkawachi.doctest

import scala.util.parsing.combinator.RegexParsers
import scala.util.parsing.input.Positional

object CommentParser extends RegexParsers {

  case class PositionedString(s: String) extends Positional

  val PYTHON_STYLE_PROMPT = ">>> "
  val REPL_STYLE_PROMPT = "scala> "
  val PROP_PROMPT = "prop> "

  lazy val eol = opt('\r') <~ '\n'

  lazy val anyLine = ".*".r <~ eol ^^ (_ => None)

  def lines = rep(importLine | example | replExample | propLine | anyLine) <~ ".*".r ^^ (_.flatten)

  lazy val leadingChar = ('/': Parser[Char]) | '*' | ' ' | '\t'

  lazy val leadingString = rep(leadingChar) ^^ (_.mkString)

  lazy val strRep1 = positioned(".+".r ^^ { PositionedString })

  lazy val importLine =
    leadingString ~> (REPL_STYLE_PROMPT | PYTHON_STYLE_PROMPT) ~> "import\\s+\\S+".r <~ eol ^^ {
      case imp => Some(Import(imp))
    }

  lazy val exprPrompt = leadingString <~ PYTHON_STYLE_PROMPT

  lazy val exprLine = exprPrompt ~ strRep1 <~ eol

  def expectedLine(leading: String) = leading ~> ".+".r <~ eol

  lazy val example = exprLine >> {
    case (leading ~ expr) =>
      expectedLine(leading) ^^ { case ex => Some(Example(expr.s, ex, expr.pos.line)) }
  }

  lazy val replPrompt = leadingString <~ REPL_STYLE_PROMPT

  lazy val replLine = replPrompt ~ strRep1 <~ eol

  def replExpectedLine(leading: String) = leading ~> "res\\d+: \\w+ = ".r ~> ".+".r <~ eol

  lazy val replExample = replLine >> {
    case (leading ~ expr) =>
      replExpectedLine(leading) ^^ { case ex => Some(Example(expr.s, ex, expr.pos.line)) }
  }

  lazy val propPrompt = leadingString <~ PROP_PROMPT

  lazy val propLine = propPrompt ~> strRep1 <~ eol ^^ (ps => Some(Prop(ps.s, ps.pos.line)))

  def parse(input: String) = parseAll(lines, input)

  def apply(input: String): Either[String, List[DoctestComponent]] with Product with Serializable = parse(input) match {
    case Success(examples, _) => Right(examples)
    case NoSuccess(msg, next) => Left(s"$msg on line ${next.pos.line}, column ${next.pos.column}")
  }

  override def skipWhitespace = false

}
