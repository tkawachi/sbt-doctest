package com.github.tkawachi.doctest

import sbt.File
import sbt.internal.io.Source
import sbt.io.{ AllPassFilter, NothingFilter }

object SbtCompat {
  def toSource(file: File): Source = new Source(file, AllPassFilter, NothingFilter)
}
