package com.github.tkawachi.doctest

import scala.util.parsing.combinator.RegexParsers
import scala.util.parsing.input.Positional

object CommentParser extends RegexParsers {

  case class PositionedString(s: String) extends Positional

  val LS = System.lineSeparator()

  val PYTHON_STYLE_PROMPT = ">>> "
  val PYTHON_CONT_PROMPT = "... "
  val REPL_STYLE_PROMPT = "scala> "
  val REPL_CONT_PROMPT = "     | "
  val PROP_PROMPT = "prop> "
  val PROP_CONT_PROMPT = "    | "

  lazy val eol = opt('\r') <~ '\n'

  lazy val anyLine = ".*".r <~ eol ^^ (_ => None)

  def lines = rep(importLine | example | replExample | prop | anyLine) <~ ".*".r ^^ (_.flatten)

  lazy val leadingChar = ('/': Parser[Char]) | '*' | ' ' | '\t'

  lazy val leadingString = rep(leadingChar) ^^ (_.mkString)

  lazy val strRep1 = positioned(".+".r ^^ { PositionedString })

  lazy val importLine =
    leadingString ~> (REPL_STYLE_PROMPT | PYTHON_STYLE_PROMPT | PROP_PROMPT) ~> "import\\s+\\S+".r <~ eol ^^ {
      case imp => Some(Import(imp))
    }

  lazy val pythonLine = (leadingString <~ PYTHON_STYLE_PROMPT) ~ strRep1 <~ eol

  def pythonContLines(leading: String) = leading ~> PYTHON_CONT_PROMPT ~> ".*".r <~ eol

  def pythonExpectedLine(leading: String) = leading ~> ".+".r <~ eol

  lazy val example = pythonLine >> {
    case leading ~ posFirstLine =>
      pythonContLines(leading).* ~ pythonExpectedLine(leading) ^^ {
        case contLines ~ ex =>
          val exprLines = (List(posFirstLine.s) ++ contLines).mkString(LS)
          Some(Example(exprLines, ex, posFirstLine.pos.line))
      }
  }

  lazy val replLine = (leadingString <~ REPL_STYLE_PROMPT) ~ strRep1 <~ eol

  def replContLine(leading: String) = leading ~> REPL_CONT_PROMPT ~> ".*".r <~ eol

  def replExpectedLine(leading: String) = leading ~> "res\\d+: \\w+ = ".r ~> ".+".r <~ eol

  lazy val replExample = replLine >> {
    case leading ~ posFirstLine =>
      replContLine(leading).* ~ replExpectedLine(leading) ^^ {
        case contLines ~ ex =>
          val exprLines = (List(posFirstLine.s) ++ contLines).mkString(LS)
          Some(Example(exprLines, ex, posFirstLine.pos.line))
      }
  }

  lazy val propLine = (leadingString <~ PROP_PROMPT) ~ strRep1 <~ eol // ^^ (ps => Some(Prop(ps.s, ps.pos.line)))

  def propContLine(leading: String) = leading ~> PROP_CONT_PROMPT ~> ".*".r <~ eol

  lazy val prop = propLine >> {
    case leading ~ posFirstLine =>
      propContLine(leading).* ^^ {
        case contLines =>
          val lines = (List(posFirstLine.s) ++ contLines).mkString(LS)
          Some(Prop(lines, posFirstLine.pos.line))
      }
  }

  def parse(input: String) = parseAll(lines, input)

  def apply(input: String): Either[String, List[DoctestComponent]] with Product with Serializable = parse(input) match {
    case Success(examples, _) => Right(examples)
    case NoSuccess(msg, next) => Left(s"$msg on line ${next.pos.line}, column ${next.pos.column}")
  }

  override def skipWhitespace = false

}
