package com.github.tkawachi

import com.github.tkawachi.doctest.DoctestPlugin.DoctestTestFramework

package object doctest {

  val testGen: Map[DoctestTestFramework, TestGen] = Map(
    DoctestTestFramework.utest -> MicroTestGen,
    DoctestTestFramework.ScalaTest -> ScalaTestGen,
    DoctestTestFramework.Specs2 -> Specs2TestGen,
    DoctestTestFramework.ScalaCheck -> ScalaCheckGen
  )

}
