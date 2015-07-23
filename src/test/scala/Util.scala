


/**
 * Created by yaroslav on 09.06.2015.*/
object Util {
  def model1 = {
    import newmodel._
    val orgFoofoo = newmodel.Decl.Def(
      "foo",
      Type.Name("String",Seq(Term.Name("java",Seq()), Term.Name("lang",Seq()), Type.Name("String",Seq()))),
      Seq(),
      Seq(),
      Comment("Stub"),
      Seq(),
      Seq(Term.Name("org",Seq()), Type.Name("Foo",Seq()), Term.Name("foo",Seq())))
    val orgFoo = newmodel.Defn.Object(
      Type.Name("Foo",Seq(Term.Name("org",Seq()), Type.Name("Foo",Seq()))) ,
      newmodel.Template(Seq(),Seq(orgFoofoo)),
      Comment("stub"),
      Seq(),
      SourceFile("")
    )
    val org = newmodel.Pkg(
      "org",
      Seq(orgFoo),
      Comment("asd"),
      Seq(Term.Name("org",Seq()))
    )
    org

  }
}


package object org {

  object Foo {
    def foo: String = ???
  }

}
