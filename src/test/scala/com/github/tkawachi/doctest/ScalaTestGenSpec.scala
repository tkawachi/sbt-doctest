package com.github.tkawachi.doctest

import sbt.Keys.moduleID
import sbt._
import utest._

object ScalaTestGenSpec extends TestSuite {
  val tests = this{
    "hasGreaterThanOrEqualTo310(3.0.0)" - {
      val result = ScalaTestGen.hasGreaterThanOrEqualTo310(
        Seq(
          Attributed(file("."))(
            AttributeMap(AttributeEntry(moduleID.key, ModuleID("org.scalatest", "scalatest_2.11", "3.0.0"))))), "2.11.12")
      assert(!result)
    }

    "hasGreaterThanOrEqualTo310(3.1.0)" - {
      val result = ScalaTestGen.hasGreaterThanOrEqualTo310(
        Seq(
          Attributed(file("."))(
            AttributeMap(AttributeEntry(moduleID.key, ModuleID("org.scalatest", "scalatest_2.11", "3.1.0"))))), "2.11.12")
      assert(result)
    }

    "hasGreaterThanOrEqualTo310() scalaVersion mismatch" - {
      val result = ScalaTestGen.hasGreaterThanOrEqualTo310(
        Seq(
          Attributed(file("."))(
            AttributeMap(AttributeEntry(moduleID.key, ModuleID("org.scalatest", "scalatest_2.11", "3.1.0"))))), "2.12.5")
      assert(!result)
    }
  }
}
