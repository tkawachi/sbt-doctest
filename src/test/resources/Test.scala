/**
 * Test class
 */
class Test {
  /**
   * A function.
   *
   * >>> new Test().f(10)
   * 20
   *
   * >>> "hello, " + "world!"
   * hello, world!
   */
  def f(x: Int) = x + x

  /** Ascii method
   * scala> new Test() += 1
   * 2
   */
  def +=(x: Int) = x + x

  /** Escape-character method
   * scala> new Test() \ 1
   * 2
   */
  def \(x: Int) = x + x
}
