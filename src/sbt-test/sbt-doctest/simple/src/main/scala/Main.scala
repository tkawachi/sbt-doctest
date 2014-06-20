package sbt_doctest

object Main {
  /**
   * A function.
   *
   * {{{
   * >>> Main.f(10)
   * 20
   *
   * prop> (i: Int) =>
   *     |   Main.f(i) should === (i *
   *     | 2)
   * }}}
   */
  def f(x: Int) = x + x

  /**
   * Comments on variables are also picked up
   *
   * {{{
   * # Import test
   * >>> import sbt_doctest.Main.xyz
   *
   * >>> xyz
   * 123
   * }}}
   */
  val xyz = 123

  /**
   * {{{
   * scala> import sbt_doctest.Main.abc
   * import sbt_doctest.Main.abc
   *
   * scala> abc
   * res0: String = Hello, world!
   * }}}
   */
  val abc = "Hello, world!"
}

