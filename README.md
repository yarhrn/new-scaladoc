Ultradoc: An experimental alternative Scaladoc implementation using Scala meta and producing latex/pdf output.

### Try it

```
git clone git@github.com:kolowheel/new-scaladoc.git
cd new-scaladoc
sbt "gendoc src/main/scala/test/"
open doc.pdf
```

Requires `pdflatex` to be installed and on the $PATH. Use [MiKTeX 2.9](http://miktex.org/) on Windows.

Or try it on a custom source directory.


### Motivation

The original Scaladoc is closely coupled to Scalac. Ultradoc explores an alternative, stand-alone implementation using
[scalameta](https://github.com/scalameta/scalameta) as a library. This should make it easier to change and extend.
One application could be adding support for alternative output formats. Currently the only supported target
format is latex/pdf.

### State of the project 

Ultradoc currently supports the full pipeline from scala source code via scala meta, doc trees and latex to pdf for a limited set of Scala language features. It's not production ready, but can serve as a basis for further development and scala meta based code tools.

Currently only source files starting with a package declaration, followed by top-level classes, objects and traits are supported. E.g.

Example
```scala
package test;

class Foo{...}
trait Bar{...}
```

### Design decisions

Ultradoc's design breaks up into frontends and backends. Frontends produce ultradoc trees, backends consume them and produce documentation formats. Currently there is only one each. A frontend that uses scala meta to produce ultradoc trees. And a backend that produces latex output from ultradoc trees, which can be turned into PDF. Other backends could be implemented to go to html, html+js, chm or other formats.

Ultradoc's trees ([newmodel/DocElement.scala](https://github.com/kolowheel/new-scaladoc/blob/master/src/main/scala/newmodel/DocElement.scala)) are closely modeled after [scalameta](https://github.com/scalameta/scalameta)'s trees. They contain additional information, for which meta would need a Scala compiler instance to compute them. Ultradoc trees can be used to generate documentation without a compiler instance.

### Contribute

The easiest way to contribute is to add support for more language features.
Another larger feature would be implementing another backend besides the latex backend and generalizing functionality out of `LatexDocGenerator` into `DocGenerator`.

### Current contributors
This project started during Google Summer of Code 2015 under the Scala Team organization.
It is developed by @kolowheel incorporating ideas by @cvogt and @clhodapp.
