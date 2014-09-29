sbt-doctest
===========

Plugin for [sbt](http://www.scala-sbt.org) that generates tests from examples
in ScalaDoc.

[![Build Status](https://travis-ci.org/tkawachi/sbt-doctest.svg?branch=master)](https://travis-ci.org/tkawachi/sbt-doctest)
[![Stories in Ready](https://badge.waffle.io/tkawachi/sbt-doctest.png?label=ready&title=Ready)](https://waffle.io/tkawachi/sbt-doctest)

Install
-------

To use this plugin, add it to your `project/plugins.sbt`,

	addSbtPlugin("com.github.tkawachi" % "sbt-doctest" % "0.1.1")

and add the following settings to your `build.sbt`.

	DoctestPlugin.doctestSettings

NOTE:
`DoctestPlugin.doctestSettings` adds scalatest and scalacheck to `libraryDependencies`.
Use `DoctestPlugin.doctestSettingsWithoutLibs` instead when you specify these in `build.sbt`.
It might be useful when you want to specify a version of scalatest/scalacheck explicitly.

	// Instead of DoctestPlugin.doctestSettings
	DoctestPlugin.doctestSettingsWithoutLibs
	
	libraryDependencies ++= Seq(
      "org.scalatest" %% "scalatest" % "2.2.0" % "test",
      "org.scalacheck" %% "scalacheck" % "1.11.4" % "test"
      // And other library dependencies.
	)

Usage
-----

sbt-doctest will generate [ScalaTest](http://www.scalatest.org) tests from
doctests in ScalaDoc comments. These tests are automatically generated and
run when sbt's `test` task is invoked.

Here is an example that shows the different doctest styles that are supported
by the plugin:

	object Test {
	
	  /**
	   * A sample function.
	   *
	   * {{{
	   * # Python style
	   * >>> Test.f(10)
	   * 20
	   *
	   * # Scala REPL style
	   * scala> Test.f(20)
	   * res1: Int = 40
	   *
	   * # Property based test
	   * prop> (i: Int) => Test.f(i) should === (i * 2)
	   * }}}
	   */
	  def f(x: Int) = x + x
	}

It also supports multi-line inputs:

	/**
	 * {{{
	 * # Python style
	 * >>> Test.f(
	 * ...   10
	 * ... )
	 * 20
	 *
	 * # Scala REPL style
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
