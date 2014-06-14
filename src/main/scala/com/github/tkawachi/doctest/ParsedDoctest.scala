package com.github.tkawachi.doctest

case class ParsedDoctest(pkg: Option[String], components: Seq[DoctestComponent], lineno: Int)
