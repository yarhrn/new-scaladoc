import org.scalatest.FunSuite


class LatexDocGeneratorTest extends FunSuite {
  lazy val model1Sample: String = io.Source.fromFile("src/test/resources/util.model1.latex").mkString
  test("basic test for latex generation") {
    //    assert(model1Sample == LatexDocGenerator.generate(Util.model1))
    println(LatexDocGenerator.generate(Util.model1))
  }

  test("basic test for dumpTypes"){
    import newmodel._
    //Int
    val intTpe = Type.Name("Int", Seq(Term.Name("scala", Seq()), Type.Name("Int", Seq())))
    assert(LatexDocGenerator.dumpType(intTpe)=="\\hyperlink{scala.Int}{Int}")

    //Seq[Int]
    val tpe = Type.Apply(
      Type.Name("Seq", Seq(Term.Name("scala", Seq()), Term.Name("collection", Seq()), Type.Name("Seq", Seq()))),
      Seq(intTpe))
    assert(LatexDocGenerator.dumpType(tpe)=="\\hyperlink{scala.collection.Seq}{Seq}[\\hyperlink{scala.Int}{Int}]")

    val `[T]` = Type.Param(Nil, Type.Name("T",Seq()), Nil, Type.Bounds(None, None), Nil, Nil)
    assert(LatexDocGenerator.dumpType(`[T]` ) == "[T]")

    //Type.Param(Nil, Type.Name("A"), Nil, Type.Bounds(None, None), List(Type.Apply(Type.Name("Ordered"), List(Type.Name("A")))), Nil)
    val `[A <% Ordered[A]]` = Type.Param(Nil, Type.Name("A",Seq()), Nil, Type.Bounds(Some(Type.Name("Int",Seq())), None), List(Type.Apply(Type.Name("CustomClass",Seq(Term.Name("org",Seq()),Type.Name("CustomClass",Seq()))), List(Type.Name("A",Seq())))), Nil)
    assert(LatexDocGenerator.dumpType(`[A <% Ordered[A]]`) == "[A >: Int<% \\hyperlink{org.CustomClass}{CustomClass}[A]]")

  }

}
