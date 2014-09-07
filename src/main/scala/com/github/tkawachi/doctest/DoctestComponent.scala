package com.github.tkawachi.doctest

sealed abstract class DoctestComponent
case class Example(expr: String, expected: TestResult, line: Int) extends DoctestComponent
case class Prop(prop: String, line: Int) extends DoctestComponent
case class Import(importLine: String) extends DoctestComponent
