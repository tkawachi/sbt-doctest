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
   * // Property check
   * prop> (s: String) => escapeDoubleQuote(s).size should be >= s.size
   * }}}
   *
   * @param s An original string.
   * @return An escaped string.
   */
  def escapeDoubleQuote(s: String): String = s.replace("\\", "\\\\").replace("\"", "\\\"")
}
