package sbt_doctest

object Main {
  /**
   * A function.
   *
   * >>> sbt_doctest.Main.f(10)
   * 20
   *
   * prop> (i: Int) => sbt_doctest.Main.f(i) should be === (i * 2)
   */
  def f(x: Int) = x + x
}

