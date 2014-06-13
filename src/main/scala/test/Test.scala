package test
package foo {

  /**
   * >>> 10 + 100
   * 110
   */
  object X {

  }
}

import scala.io.Source
import scala.tools.nsc.doc.{ Settings, DocParser }
import java.io.File
import com.github.tkawachi.doctest.{ ScalaTestGen, Extractor }
import com.github.tkawachi.doctest.Gen

/**
 * Hoge fuga--
 */
class Test {

}

object Test {

  /**
   * A sample function.
   *
   * {{{
   * # Python style
   * >>> Test.f(10)
   * 20
   *
   * # Scala repl style
   * scala> Test.f(20)
   * res1: Int = 40
   *
   * # Property based test
   * prop> (i: Int) => Test.f(i) should be === (i * 2)
   * }}}
   */
  def f(x: Int) = x + x

  /* He- Fuu- */
  val x = 1

  /**
   * Test main.
   *
   * {{{
   * >>> 1 + 1
   * 2
   * }}}
   *
   * >>> 100 + 1
   * 101
   *
   * >>> "abc".substring(10)
   *
   * x prop> (i: Int) => Math.abs(i) should be >= 0
   * x prop> (i: Int) => i + i should be > i
   *
   * @example 100 + 1
   *          101
   * @param args
   * @return
   */
  def main(args: Array[String]): Unit = {
    /**
     * How about this?
     */
    val file = new File("src/main/scala/DoctestPlugin.scala")
    println(Gen.gen(file))
  }
}
