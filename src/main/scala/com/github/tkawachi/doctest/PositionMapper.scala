package com.github.tkawachi.doctest

import sbt._
import xsbti.Position

object PositionMapper {
  val mapper: Position => Option[Position] = position => {
    position.sourceFile collect {
      case GeneratedSource(generatedSource) =>
        new xsbti.Position {
          lazy val line = {
            position.line.flatMap(l => generatedSource.mapLine(l.asInstanceOf[Int])).map(l => xsbti.Maybe.just(l.asInstanceOf[java.lang.Integer])).getOrElse(xsbti.Maybe.nothing[java.lang.Integer])
          }
          lazy val lineContent = {
            line flatMap { lineNo =>
              sourceFile.flatMap { file =>
                IO.readLines(file).lift(lineNo - 1)
              }
            } getOrElse ""
          }
          val offset = xsbti.Maybe.nothing[java.lang.Integer]
          val pointer = xsbti.Maybe.nothing[java.lang.Integer]
          val pointerSpace = xsbti.Maybe.nothing[String]
          val sourceFile = xsbti.Maybe.just(generatedSource.source.get)
          val sourcePath = xsbti.Maybe.just(sourceFile.get.getCanonicalPath)
        }
    }
  }
}
