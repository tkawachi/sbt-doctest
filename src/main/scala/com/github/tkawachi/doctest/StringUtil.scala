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
   *     |   escapeDoubleQuote(s).filter(_ == '"').size >= s.filter(_ == '"').size
   * }}}
   *
   * @param s An original string.
   * @return An escaped string.
   */
  def escapeDoubleQuote(s: String): String = s.replace("\\", "\\\\").replace("\"", "\\\"")

  /**
   * {{{
   * >>> import StringUtil._
   * >>> escapeLineSep("Hello,\nWorld")
   * Hello,\nWorld
   *
   * >>> escapeLineSep("Hello,\rWorld")
   * Hello,\rWorld
   * }}}
   */
  def escapeLineSep(s: String): String = s.replace("\n", "\\n").replace("\r", "\\r")

  def escape(s: String): String = escapeLineSep(escapeDoubleQuote(s))

  /**
   * {{{
   * >>> StringUtil.indent("Hello\nWorld", "[indent]")
   * [indent]Hello
   * [indent]World
   * }}}
   */
  def indent(target: String, indentation: String): String = {
    val pattern = """(\n\r?)""".r
    indentation + pattern.replaceAllIn(target, _.group(0) + indentation)
  }

  /**
   * {{{
   * prop> (s: String) => s.startsWith(StringUtil.truncate(s).dropRight(4))
   * }}}
   */
  def truncate(s: String): String = {
    val maxLength = 60
    val lines = s.split("[\\r\\n]")
    val suffix =
      if (lines.size > 1 || lines.headOption.exists(_.length > maxLength)) " ..."
      else ""

    lines.headOption.fold("")(_.take(maxLength) + suffix)
  }

  /**
   * Obtains the base name of a file, behaving exactly as ``FilenameUtils.getBaseName`` from Apache Commons-IO did.
   *
   * {{{
   * >>> StringUtil.getBaseName("/etc/profile.d/bash_completion.sh")
   * /etc/profile.d/bash_completion
   *
   * >>> StringUtil.getBaseName("/etc/profile.d")
   * /etc/profile
   *
   * >>> StringUtil.getBaseName("/etc/profile.d/")
   * /etc/profile.d/
   * }}}
   */
  def getBaseName(filename: String): String = {
    if (filename == null) null else {
      if (filename.indexOf(0) > -1)
        throw new IllegalArgumentException("Null byte present in file/path name. There are no known legitimate use cases for such data, but several injection attacks may use it")
      val extensionPos = filename.lastIndexOf('.')
      val lastSeparator = filename.lastIndexOf(java.io.File.separatorChar)
      if (lastSeparator > extensionPos) filename else filename.substring(0, extensionPos)
    }
  }

  /**
   * Unescapes HTML4 content, behaving exactly as ``unescapeHtml4`` from Apache Lang3 did.
   */
  def unescapeHtml4(input: String): String = UNESCAPE_HTML4.translate(input)

  private val UNESCAPE_HTML4 = {
    import org.apache.commons.text.translate.AggregateTranslator
    import org.apache.commons.text.translate.EntityArrays
    import org.apache.commons.text.translate.LookupTranslator
    import org.apache.commons.text.translate.NumericEntityUnescaper
    new AggregateTranslator(
      new LookupTranslator(EntityArrays.BASIC_UNESCAPE),
      new LookupTranslator(EntityArrays.ISO8859_1_UNESCAPE),
      new LookupTranslator(EntityArrays.HTML40_EXTENDED_UNESCAPE),
      new NumericEntityUnescaper)
  }

}
