package com.github.tkawachi.doctest

case class ParsedDoctest(pkg: String, components: Seq[DoctestComponent], lineno: Int)
