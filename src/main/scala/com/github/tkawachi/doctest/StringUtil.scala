package com.github.tkawachi.doctest

object StringUtil {
  /**
   * Escape a string.
   *
   * {{{
   * scala> import com.github.tkawachi.doctest.StringUtil._
   * import com.github.tkawachi.doctest.StringUtil._
   *
   * scala> escapeDoubleQuote("""aaa"bbb\cc"""")
   * res2: String = aaa\"bbb\\cc\"
   *
   * >>> escapeDoubleQuote("""aaa"bbb\cc"""")
   * aaa\"bbb\\cc\"
   * }}}
   *
   * @param s An original string.
   * @return An escaped string.
   */
  def escapeDoubleQuote(s: String): String = s.replace("\\", "\\\\").replace("\"", "\\\"")
}
