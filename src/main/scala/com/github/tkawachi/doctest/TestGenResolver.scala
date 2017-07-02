package com.github.tkawachi.doctest

import com.github.tkawachi.doctest.DoctestPlugin.DoctestTestFramework
import com.github.tkawachi.doctest.DoctestPlugin.DoctestTestFramework._
import sbt.Keys.Classpath

object TestGenResolver {
  def resolve(framework: DoctestTestFramework, testClasspath: Classpath, scalaVersion: String): TestGen = {
    framework match {
      case `utest` => MicroTestGen
      case ScalaTest =>
        if (ScalaTestGen.hasGreaterThanOrEqualTo310(testClasspath, scalaVersion)) {
          new ScalaTest31Gen
        } else {
          new ScalaTest30Gen
        }
      case Specs2 => Specs2TestGen
      case ScalaCheck => ScalaCheckGen
    }
  }
}
