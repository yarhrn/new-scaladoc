
### Motivation

This is base project for new scaladoc implementation. 
It is intended to give an opportunity to easily add new output formats.

### Project description

There is new model that is independetly represent source for documenting.
For now this model is generating using [scalameta](https://github.com/scalameta/scalameta) api, but it could be generated from any source(compielr plugin, etc.).
Model locates in [newmodel/DocElement.scala](https://github.com/kolowheel/new-scaladoc/blob/master/src/main/scala/newmodel/DocElement.scala). 
Current version could generate limited pdf(latex) documentation.

Supported file format is : declarated in package classes,traits and objects.
Example
```scala
package test;

class Foo{}
trait Bar{}
```


### How to run

##### For Windows:

Note: [MiKTeX 2.9](http://miktex.org/) should be installed and added to path

```
sbt "gendoc path/to/src"
```


##### For Unix-based:
```
sbt "gendoc "
```








