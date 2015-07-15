


/**
 * Created by yaroslav on 09.06.2015.*/
object Util {
  def model1 = {
    import newmodel._
    val orgFoofoo = newmodel.Decl.Def(
      "foo",
      Seq(Term.Name("java"), Term.Name("lang"), Type.Name("String")),
      Seq(),
      Seq(),
      Comment("Stub"),
      Seq(),
      Seq(Term.Name("org"), Type.Name("Foo"), Term.Name("foo")))
    val orgFoo = newmodel.Defn.Object(
      "Foo",
      newmodel.Template(Seq(orgFoofoo)),
      Comment("stub"),
      Seq(),
      SourceFile(""),
      Seq(Term.Name("org"), Type.Name("Foo"))
    )
    val org = newmodel.Pkg(
      "org",
      Seq(orgFoo),
      Comment("asd"),
      Seq(Term.Name("org"))
    )
    org
  }
}


package object org {

  object Foo {
    def foo: String = ???
  }

}
