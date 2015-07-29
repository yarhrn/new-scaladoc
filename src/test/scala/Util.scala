


/**
 * Created by yaroslav on 09.06.2015.*/
object Util {
  def model1 = {
    import newmodel._


    val declType = Decl.Type(
      Seq(),
      Type.Name("Queue", Seq(Term.Name("org", Seq()), Type.Name("Bar", Seq()), Term.Name("Queue", Seq()))),
      Seq(),
      Type.Bounds(None, None)
    )
    val defnType = Defn.Type(
      Seq(),
      Type.Name("Queue", Seq(Term.Name("org", Seq()), Type.Name("Foo", Seq()), Term.Name("Queue", Seq()))),
      Seq(),
      Type.Apply(
        Type.Name("Seq", Seq(Term.Name("scala", Seq()), Term.Name("collection", Seq()), Type.Name("Seq", Seq()))),
        Seq(Type.Name("Int", Seq()))
      )
    )

    val orgFooFoo = newmodel.Decl.Def(
      "foo",
      Type.Name("SomeTrait", Seq(Term.Name("org", Seq()), Term.Name("SomeTrait", Seq()))),
      Seq(),
      Seq(),
      Comment("Stub"),
      Seq(),
      Seq(Term.Name("org", Seq()), Type.Name("Foo", Seq()), Term.Name("foo", Seq())))
    val orgFooHello = newmodel.Decl.Def(
      "hello",
      Type.Name("SomeTrait", Seq(Term.Name("org", Seq()), Term.Name("SomeTrait", Seq()))),
      Seq(),
      Seq(),
      Comment("Hello method"),
      Seq(newmodel.Mod.Override),
      Seq(Term.Name("org", Seq()), Type.Name("Foo", Seq()), Term.Name("hello", Seq())))
    val orgFoo = newmodel.Defn.Object(
      Type.Name("Foo", Seq(Term.Name("org", Seq()), Type.Name("Foo", Seq()))),
      newmodel.Template(Seq(Type.Name("Bar", Seq(Term.Name("org", Seq()), Type.Name("Bar", Seq())))), Seq(orgFooFoo, orgFooHello,defnType)),
      Comment("stub"),
      Seq(),
      SourceFile("")
    )


    val orgBarHello = newmodel.Decl.Def(
      "hello",
      Type.Name("SomeTrait", Seq(Term.Name("org", Seq()), Term.Name("SomeTrait", Seq()))),
      Seq(),
      Seq(),
      Comment("Hello method"),
      Seq(),
      Seq(Term.Name("org", Seq()), Type.Name("Bar", Seq()), Term.Name("hello", Seq())))
    val orgBar = newmodel.Defn.Trait(
      Type.Name("Bar", Seq(Term.Name("org", Seq()), Type.Name("Bar", Seq()))),
      newmodel.Template(Seq(), Seq(orgBarHello)),
      Comment("Bar trait"),
      Seq(),
      Seq(),
      SourceFile("")
    )

    val orgSomeTrait = newmodel.Defn.Trait(
      Type.Name("SomeTrait", Seq(Term.Name("org", Seq()), Type.Name("SomeTrait", Seq()))),
      newmodel.Template(Seq(), Seq(orgBarHello, declType)),
      Comment("SomeTrait trait"),
      Seq(),
      Seq(),
      SourceFile("")
    )




    val org = newmodel.Pkg(
      "org",
      Seq(orgFoo, orgBar, orgSomeTrait),
      Comment("asd"),
      Seq(Term.Name("org", Seq()))
    )
    org

  }
}


package object org {

  object Foo extends Bar {
    def foo: String = ""

    override def hello: SomeTrait = ???
  }

  trait Bar {
    def hello: SomeTrait
  }

  trait SomeTrait {

  }

}
