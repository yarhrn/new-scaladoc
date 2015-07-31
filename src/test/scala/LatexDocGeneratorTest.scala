import org.scalatest.FunSuite


class LatexDocGeneratorTest extends FunSuite {
  lazy val model1Sample: String = io.Source.fromFile("src/test/resources/util.model1.latex").mkString
  test("basic test for latex generation") {

    println(new LatexDocGenerator(Index(Util.model1)).generate(Util.model1))
  }

  test("basic test for dumpTypes") {
    import newmodel._
    val latexGenerator = new LatexDocGenerator(Index(newmodel.Pkg("foo", Seq(), Comment(""), Seq())))
    //Int
    val intTpe = Type.Name("Int", Seq(Term.Name("scala", Seq()), Type.Name("Int", Seq())))
    assert(latexGenerator.dumpType(intTpe) == "\\hyperlink{scala.Int}{Int}")

    //Seq[Int]
    val tpe = Type.Apply(
      Type.Name("Seq", Seq(Term.Name("scala", Seq()), Term.Name("collection", Seq()), Type.Name("Seq", Seq()))),
      Seq(intTpe))
    assert(latexGenerator.dumpType(tpe) == "\\hyperlink{scala.collection.Seq}{Seq}[\\hyperlink{scala.Int}{Int}]")

    val `[T]` = Type.Param(Nil, Type.Name("T", Seq()), Nil, Type.Bounds(None, None), Nil, Nil)
    assert(latexGenerator.dumpType(`[T]`) == "[T]")

    val `[A >: Int <% CustomClass[A]]` = Type.Param(
      Nil,
      Type.Name("A", Seq()),
      Nil,
      Type.Bounds(Some(Type.Name("Int", Seq())), None),
      List(Type.Apply(Type.Name("CustomClass", Seq(Term.Name("org", Seq()), Type.Name("CustomClass", Seq()))), List(Type.Name("A", Seq())))), Nil)
    assert(latexGenerator.dumpType(`[A >: Int <% CustomClass[A]]`) == "[A >: Int<% \\hyperlink{org.CustomClass}{CustomClass}[A]]")

  }

  test("link superclass test") {
    import newmodel._
    val trt = Defn.Trait(
      Type.Name("Bar", Seq()),
      Template(Seq(Type.Name("Foo", Seq(Term.Name("org", Seq()), Type.Name("Foo", Seq())))), Seq()),
      Comment(""),
      Seq(),
      Seq(),
      SourceFile("s")
    )

    val orgFoofoo = newmodel.Decl.Def(
      "foo",
      Type.Name("String", Seq(Term.Name("java", Seq()), Term.Name("lang", Seq()), Type.Name("String", Seq()))),
      Seq(),
      Seq(),
      Comment("Stub"),
      Seq(),
      Seq(Term.Name("org", Seq()), Type.Name("Foo", Seq()), Term.Name("foo", Seq())))
    val orgFoo = newmodel.Defn.Trait(
      Type.Name("Foo", Seq(Term.Name("org", Seq()), Type.Name("Foo", Seq()))),
      newmodel.Template(Seq(), Seq(orgFoofoo)),
      Comment("stub"),
      Seq(),
      Seq(),
      SourceFile("")
    )
    val org = newmodel.Pkg(
      "org",
      Seq(orgFoo),
      Comment("asd"),
      Seq(Term.Name("org", Seq()))
    )

    val gen = new LatexDocGenerator(Index(org))
    println(gen.processTrait(trt))
  }


  test("type abstract member support") {
    import newmodel._
    val latexGenerator = new LatexDocGenerator(Index(newmodel.Pkg("foo", Seq(), Comment(""), Seq())))
    val `type z` = Decl.Type(Nil, Type.Name("z", Seq()), Nil, Type.Bounds(None, None))
    println(latexGenerator.dumpAbstractTypeMember(`type z`))
  }


  test("type member support") {
    import newmodel._
    val latexGenerator = new LatexDocGenerator(Index(newmodel.Pkg("foo", Seq(), Comment(""), Seq())))
    val `Int` = Type.Name("Int", Seq(Term.Name("scala", Seq()), Type.Name("Int", Seq())))
    val `Seq[Int]` = Type.Apply(
      Type.Name("Seq", Seq(Term.Name("scala", Seq()), Term.Name("collection", Seq()), Type.Name("Seq", Seq()))),
      Seq(`Int`))
    val `type z = Seq[Int]` = Defn.Type(Nil, Type.Name("z", Seq()), Nil, `Seq[Int]`)
    println(latexGenerator.dumpTypeMember(`type z = Seq[Int]`))
  }


  test("nested support") {
    import newmodel._
    val `B type name` = Type.Name("B", Seq(Type.Name("A", Seq()), Type.Name("B", Seq())))
    val `C type name` = Type.Name("C", Seq(Type.Name("A", Seq()), Type.Name("B", Seq()), Type.Name("C", Seq())))
    val `D type name` = Type.Name("D", Seq(Type.Name("A", Seq()), Type.Name("B", Seq()), Type.Name("C", Seq()), Type.Name("D", Seq())))
    val `E type name` = Type.Name("E", Seq(Type.Name("A", Seq()), Type.Name("B", Seq()), Type.Name("C", Seq()), Type.Name("D", Seq()), Type.Name("E", Seq())))
    val latexGenerator = new LatexDocGenerator(Index(newmodel.Pkg("foo", Seq(), Comment(""), Seq())))

    class A{ class B{ class C{ class D{ class E}}}}
    val teamplate = Template(
      Seq(),
      Seq(Defn.Class(
        `B type name`,
        None,
        Comment(""),
        Seq(),
        Seq(),
        SourceFile("asd"),
        Template(Seq(),
          Seq(Defn.Class(
            `C type name`,
            None,
            Comment(""),
            Seq(),
            Seq(),
            SourceFile("asd"),
            Template(Seq(),
              Seq(Defn.Class(
                `D type name`,
                None,
                Comment(""),
                Seq(),
                Seq(),
                SourceFile("asd"),
                Template(Seq(),
                  Seq(Defn.Class(
                    `E type name`,
                    None,
                    Comment(""),
                    Seq(),
                    Seq(),
                    SourceFile("asd"),
                    Template(Seq(),
                      Seq()),
                    None
                  ))),
                None
              ))),
            None
          ))),
        None
      )
      )
    )


    println(latexGenerator.dumpNested("A",teamplate)._2)
  }
}
