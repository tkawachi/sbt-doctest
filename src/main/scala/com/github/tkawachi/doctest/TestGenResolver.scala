package com.github.tkawachi.doctest

import com.github.tkawachi.doctest.DoctestPlugin.DoctestTestFramework
import com.github.tkawachi.doctest.DoctestPlugin.DoctestTestFramework._
import sbt.Keys.Classpath

object TestGenResolver {
  def resolve(framework: DoctestTestFramework, testClasspath: Classpath, scalaVersion: String): TestGen = {
    framework match {
      case MicroTest => MicroTestGen
      case Minitest => MinitestGen
      case ScalaTest =>
        if (ScalaTestGen.hasGreaterThanOrEqualTo310(testClasspath, scalaVersion)) {
          ScalaTest31Gen
        } else {
          ScalaTest30Gen
        }
      case Specs2 => Specs2TestGen
      case ScalaCheck => ScalaCheckGen
    }
  }
}
