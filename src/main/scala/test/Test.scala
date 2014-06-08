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
   * @example 100 + 1
   *          101
   * @param args
   * @return
   */
  def main(args: Array[String]): Unit = {
    /**
     * How about this?
     */
    //    val code = Source.fromFile("src/main/scala/test/Test.scala")
    //    val settings = new Settings(Console println _)
    //    //    settings processArgumentString "-usejavacp"
    //    val pathList = compilerPath ::: libPath
    //    settings.bootclasspath.value = pathList.mkString(File.pathSeparator)
    //    val parser = new DocParser(settings)
    //
    //    parser.docDefs(code.mkString).foreach { parsed =>
    //      println(parsed.nameChain)
    //      println(parsed.toString)
    //      println(parsed.docDef.definition.pos)
    //      println(parsed.docDef.definition.pos.line)
    //      println(parsed.docDef.comment.pos.line)
    //      println(parsed.raw)
    //    }
    //    val extractor = new Extractor
    //    val r = extractor.extract(code.mkString)
    //    r.groupBy(_.pkg).foreach {
    //      case (pkg, examples) =>
    //        val src = ScalaTestGen.generate("Test", pkg, examples)
    //        println(src)
    //    }

    //    val file = new File("src/main/scala/test/Test.scala")
    //    val file = new File("src/main/scala/com/github/tkawachi/doctest/Example.scala")
    val file = new File("src/main/scala/DoctestPlugin.scala")
    println(Gen.gen(file))
  }
}