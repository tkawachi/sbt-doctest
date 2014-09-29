package com.github.tkawachi.doctest

object StringUtil {
  /**
   * Escape a string.
   *
   * {{{
   * // In scala repl style
   * scala> import com.github.tkawachi.doctest.StringUtil._
   * import com.github.tkawachi.doctest.StringUtil._
   *
   * scala> escapeDoubleQuote("""aaa"bbb\cc"""")
   * res2: String = aaa\"bbb\\cc\"
   *
   * // In Python style
   * >>> escapeDoubleQuote("""aaa"bbb\cc"""")
   * aaa\"bbb\\cc\"
   *
   * // Number of double quotes will be not changed.
   * prop> (s: String) =>
   *     |   escapeDoubleQuote(s).filter(_ == '"').size should be >= s.filter(_ == '"').size
   * }}}
   *
   * @param s An original string.
   * @return An escaped string.
   */
  def escapeDoubleQuote(s: String): String = s.replace("\\", "\\\\").replace("\"", "\\\"")

  def indent(target: String, indentation: String): String = {
    val pattern = """(\n\r?)""".r
    indentation + pattern.replaceAllIn(target, _.group(0) + indentation)
  }
}
