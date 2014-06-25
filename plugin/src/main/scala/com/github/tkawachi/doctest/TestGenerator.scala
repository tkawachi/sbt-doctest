package com.github.tkawachi.doctest

object TestGenerator {
  case class Result(pkg: Option[String], basename: String, testSource: String)

  def apply(basename: String, scaladocComments: Seq[ScaladocComment]): Seq[Result] = {
    scaladocComments.flatMap(c => CommentParser(c).right.toOption)
      .groupBy(_.pkg).map {
        case (pkg, examples) =>
          val optPkg = if (pkg == "") None else Some(pkg)
          Result(optPkg, basename, ScalaTestGen.generate(basename, optPkg, examples))
      }.toSeq
  }
}
