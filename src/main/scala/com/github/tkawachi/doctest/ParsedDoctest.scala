package com.github.tkawachi.doctest

import com.github.tkawachi.doctest.CommentParser.DoctestComponent

case class ParsedDoctest(pkg: Option[String], components: Seq[DoctestComponent], lineno: Int)
