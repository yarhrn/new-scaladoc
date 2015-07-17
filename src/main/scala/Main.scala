
object Main {
  def main(args: Array[String]): Unit = {

    import newmodel._
    //Int
    val intTpe = Type.Name("Int", Seq(Term.Name("scala", Seq()), Type.Name("Int", Seq())))
    println(LatexDocGenerator.dumpType(intTpe))

    //Seq[Int]
    val tpe = Type.Apply(
      Type.Name("Seq", Seq(Term.Name("scala", Seq()), Term.Name("collection", Seq()), Type.Name("Seq", Seq()))),
      Seq(intTpe))
    println(LatexDocGenerator.dumpType(tpe))

    val `[T]` = Type.Param(Nil, Type.Name("T",Seq()), Nil, Type.Bounds(None, None), Nil, Nil)
    println(LatexDocGenerator.dumpType(`[T]` ))

    //Type.Param(Nil, Type.Name("A"), Nil, Type.Bounds(None, None), List(Type.Apply(Type.Name("Ordered"), List(Type.Name("A")))), Nil)
    val `[A <% Ordered[A]]` = Type.Param(Nil, Type.Name("A",Seq()), Nil, Type.Bounds(Some(Type.Name("Int",Seq())), None), List(Type.Apply(Type.Name("CustomClass",Seq(Term.Name("org",Seq()),Type.Name("CustomClass",Seq()))), List(Type.Name("A",Seq())))), Nil)
    println(LatexDocGenerator.dumpType(`[A <% Ordered[A]]`))


  }
}


