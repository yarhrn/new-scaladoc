

import newmodel.{Package, _}


object Main {

  def buildModel = {

    val returnForMethod1 = Type("String")
    val fooMethod = MethodDoc("foo", returnForMethod1, Seq(ValueParam("a", returnForMethod1,
      Option.empty[Implicit], ID("testId"), Comment("cool method!"))), ID("org.bar.Test#foo"), Comment("Cool method!"), Seq.empty[Flag])
    val fooMethod1 = MethodDoc("foo1", returnForMethod1, Seq(), ID("org.bar.Test#foo"), Comment("Another cool method!"), Seq())
    val testObject = ObjectDoc("Test", Seq(fooMethod, fooMethod1), ID(""), Comment("Awesome test class with two methods"), SourceFile("test1"))
    val barMethod = MethodDoc("bar", returnForMethod1, Seq(ValueParam("a", returnForMethod1, Option.empty[Implicit], ID("testId"),
      Comment("cool method!"))), ID("org.bar.Test#bar"), Comment("Cool method! of bar"), Seq.empty[Flag])
    val bar1Method = MethodDoc("bar1", returnForMethod1, Seq(), ID("org.bar.Test#bar1"), Comment("Another cool method of bar!"), Seq(Implicit()))
    val barObject = ObjectDoc("Bar", Seq(barMethod, bar1Method), ID(""), Comment("Awesome bar object with two methods"), SourceFile("test"))
    val orgPackage = Package("org", List(testObject, barObject), ID("org"), Comment("Lorem ipsum!"))
    val rootPackage = Package("_root_", List(orgPackage), ID("_root_"), Comment(""))
    rootPackage
  }


  def main(args: Array[String]): Unit = {
    val rootPackage = buildModel

    println(LatexDocGenerator.generate(rootPackage, Index(Seq(rootPackage))))
  }

}


