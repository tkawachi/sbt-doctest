[![Build Status](https://travis-ci.org/tkawachi/sbt-doctest.svg?branch=master)](https://travis-ci.org/tkawachi/sbt-doctest)
[![Stories in Ready](https://badge.waffle.io/tkawachi/sbt-doctest.png?label=ready&title=Ready)](https://waffle.io/tkawachi/sbt-doctest)

In your `project/plugins.sbt`

	addSbtPlugin("com.github.tkawachi" % "sbt-doctest" % "0.0.5")

and `build.sbt`

	DoctestPlugin.doctestSettings

It generates ScalaTest tests when it finds doctests in scaladoc comment.

A sample doctest.

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
	   * prop> (i: Int) => Test.f(i) should === (i * 2)
	   * }}}
	   */
	  def f(x: Int) = x + x
	}

It supports multi-lines inputs.

	/**
	 * {{{
	 * # Python style
	 * >>> Test.f(
	 * ...   10
	 * ... )
	 * 20
	 *
	 * # Scala repl style
	 * scala> Test.f(
	 *      |   20
	 *      | )
	 * res1: Int = 40
	 *
	 * # Property based test
	 * prop> (i: Int) =>
	 *     |   Test.f(i) should === (i * 2)
	 * }}}
	 */
	def f(x: Int) = x + x
