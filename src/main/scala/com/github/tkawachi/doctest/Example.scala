package com.github.tkawachi.doctest

/**
 *
 * {{{
 * >>> Example(Some("aaa"), "bbb", "ccc", 10).expr
 * bbb
 * }}}
 */
case class Example(pkg: Option[String], expr: String, expected: String, lineno: Int)
