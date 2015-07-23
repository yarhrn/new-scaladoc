import newmodel._


case class Index(root: Pkg) {
  val linear = loop(root)

  def classes = linear.collect { case e: Defn.Class => e }

  def objects = linear.collect { case e: Defn.Object => e }

  def traits = linear.collect { case e: Defn.Trait => e }

  def defs = linear.collect { case e: Decl.Def => e }

  def getByLink(id:Seq[Tree]) : Option[Tree] = {
    linear.find{
      case e:newmodel.Defn.Object => e.name.id == id
      case e:newmodel.Defn.Class => e.name.id == id
      case e:newmodel.Defn.Trait => e.name.id == id
      case _ => false
    }
  }

  private def mapper(stats: Seq[Stat]): Seq[Tree] = stats.flatMap(loop(_))

  private def loop(node: Tree): Seq[Tree] = {
    val nodes: Seq[Tree] = node match {
      case e: Defn.Class => mapper(e.templ.stats)
      case e: Defn.Trait => mapper(e.templ.stats)
      case e: Defn.Object => mapper(e.templ.stats)
      case e: Pkg => mapper(e.stats)
      case e: Decl.Def => Seq(e)
      case _ => Seq()
    }
    node match {
      case _: Defn.Class | _: Defn.Trait | _: Defn.Object | _: Pkg | _:Decl.Def => nodes ++ Seq(node)
      case _ => nodes
    }
  }
}