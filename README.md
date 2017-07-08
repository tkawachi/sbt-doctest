# sbt-doctest

Plugin for [sbt](http://www.scala-sbt.org) that generates tests from examples
in ScalaDoc.

[![Build Status](https://travis-ci.org/tkawachi/sbt-doctest.svg?branch=master)](https://travis-ci.org/tkawachi/sbt-doctest)
[![Codacy Badge](https://www.codacy.com/project/badge/69a7c0f566464cc38032d10d3b9dab6c)](https://www.codacy.com/app/tkawachi/sbt-doctest)

## Install

To use this plugin, add it to your `project/plugins.sbt`.

```scala
addSbtPlugin("com.github.tkawachi" % "sbt-doctest" % "0.6.0")
```

It's automatically enabled for JVM projects.
Scala.js is currently not supported (See #52).

### Using ScalaTest

If you are using ``ScalaTest``, add the following lines to your ``build.sbt``:

```scala
libraryDependencies ++= Seq(
  "org.scalatest"  %% "scalatest"  % "3.0.1"  % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
)

doctestTestFramework := DoctestTestFramework.ScalaTest
```

### Using ScalaCheck

If you are using ``ScalaCheck``, add the following lines to your ``build.sbt``:

```scala
libraryDependencies ++= Seq(
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
)

doctestTestFramework := DoctestTestFramework.ScalaCheck
```

### Using Specs2

If you are using ``Specs2``, add the following lines to your ``build.sbt``:

```scala
libraryDependencies ++= Seq(
  "org.specs2" %% "specs2-core"       % "3.8.7" % "test",
  "org.specs2" %% "specs2-scalacheck" % "3.8.7" % "test"
)

doctestTestFramework := DoctestTestFramework.Specs2
```

### Using µTest

If you are using ``µTest``, add the following lines to your ``build.sbt``:
```scala
libraryDependencies ++= Seq(
  "com.lihaoyi" %% "utest" % "0.4.7" % "test"
)

doctestTestFramework := DoctestTestFramework.MicroTest
```

#### Caveats

There are still dependencies from ``ScalaTest`` and/or ``ScalaCheck`` when property checks are employed.

The difficulty can be circumvented for the time being by providing all dependencies in ``build.sbt``, like
shown in the example below which uses ``uTest`` with property checks, which require ``ScalaTest`` and ``ScalaCheck`` as well:

```scala
libraryDependencies ++= Seq(
  "com.lihaoyi"    %% "utest"      % "0.4.7"  % "test",
  "org.scalatest"  %% "scalatest"  % "3.0.1"  % "test",
  "org.scalacheck" %% "scalacheck" % "1.13.4" % "test"
)
      
doctestTestFramework := DoctestTestFramework.MicroTest
```

## Usage

sbt-doctest will generate tests from
doctests in ScalaDoc comments. These tests are automatically generated and
run when sbt's `test` task is invoked.

Here is an example that shows the different doctest styles that are supported
by the plugin:

```scala
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
   * prop> (i: Int) => Test.f(i) == (i * 2)
   * }}}
   */
  def f(x: Int) = x + x
}
```

It also supports multi-line inputs:

```scala
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
 *     |   Test.f(i) == (i * 2)
 * }}}
 */
def f(x: Int) = x + x
```

Please use `<BLANKLINE>` when an output contains blank lines.

```scala
/**
 * {{{
 * # Python style
 * >>> Test.helloWorld
 * Hello
 * <BLANKLINE>
 * World
 *
 * # Scala REPL style
 * scala> Test.helloWorld
 * res0: String =
 * Hello
 * <BLANKLINE>
 * World
 * }}}
 */
def helloWorld = "Hello\n\nWorld"
```
## HTML Entities

Often when documenting libraries that work with HTML you need to encode HTML entities so that they will be displayed in browsers.

However, `sbt-doctest` ignores these and attempts to compare encoded HTML with unencoded HTML entities. You can fix this by enabling decoding of HTML entities. Just add the following setting to your `build.sbt`:

```scala
doctestDecodeHtmlEntities := true
```

Now the following should pass:

```scala
  /**
   * {{{
   * >>> Main.html
   * &lt;html&gt;&lt;/html&gt;
   * }}}
   */
  val html = "<html></html>"
```

## Markdown

Also supports code examples in Markdown documentation. To enable add the following to your `build.sbt`:

```scala
doctestMarkdownEnabled := true
```

Any code blocks that start with the ````scala` markdown directive will be parsed.
It searches `*.md` under `baseDirectory` by default. It can be configured by
`doctestMarkdownPathFinder`.

```scala
// default
doctestMarkdownPathFinder := baseDirectory.value * "*.md"

// search doc/ recursively
doctestMarkdownPathFinder := baseDirectory.value * "doc" ** "*.md" 
```

See [an example markdown](https://github.com/tkawachi/sbt-doctest/blob/master/src/test/resources/ScalaText.md).

## License

MIT
