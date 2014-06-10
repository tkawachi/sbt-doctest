This project is not yet published. Try with `publishLocal`.

	sbt publishLocal

In your `project/plugins.sbt`

	addSbtPlugin("com.github.tkawachi" % "sbt-doctest" % "0.1-SNAPSHOT")

and `build.sbt`

	DoctestPlugin.doctestSettings

It generates ScalaTest tests when it finds doctests in scaladoc comment.

A sample doctest.

	/**
	 * Sample function
	 *
	 * {{{
	 * >>> f(10)
	 * 20
	 * }}}
	 */
	def f(x: Int) = x + x
