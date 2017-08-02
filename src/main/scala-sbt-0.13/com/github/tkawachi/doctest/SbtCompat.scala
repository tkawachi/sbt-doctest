package com.github.tkawachi.doctest

import sbt.File

object SbtCompat {
  def toSource(file: File): File = file
}
