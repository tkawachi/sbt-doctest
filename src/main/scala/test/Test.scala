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
