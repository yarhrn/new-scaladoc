

import newmodel.Decl.Def
import newmodel.Defn.{ObjectDoc, Package}
import newmodel.{Name, _}

/**
 * Created by yaroslav on 09.06.2015.*/
object Util {
  def model1 = {
    val returnForMethod1 = Type.Name("String")
    val fooMethod = Def(Name("foo", 1), Type.Name("String"), Seq(Term.Param("a", Seq(), Type.Name("String"))), Seq(), Comment("Cool method!"), Seq())
    val fooMethod1 = Def(Name("foo12", 2), Type.Name("String"), Seq(Term.Param("a", Seq(), Type.Name("Any"))), Seq(), Comment("good method!"), Seq())
    val testObject = ObjectDoc(Name("Test", 23123), Seq(fooMethod, fooMethod1), Comment("Awesome test class with two methods"), Seq(), SourceFile("test1"))

    val barMethod = Def(Name("bar", 2), Type.Name("AnyRef"), Seq(Term.Param("z", Seq(), Type.Name("Object"))), Seq(), Comment("good method2!"), Seq())
    val bar1Method = Def(Name("bar23", 2), Type.Name("Object"), Seq(), Seq(), Comment("good method3!"), Seq())
    val barObject = ObjectDoc(Name("Bar", 115), Seq(barMethod, bar1Method), Comment("Awesome bar object with two methods"), Seq(), SourceFile("test"))
    val orgPackage = Package(Name("org", 213), List(testObject, barObject), Comment("Lorem ipsum!"))
    val rootPackage = Package(Name("_root_", 33), List(orgPackage), Comment(""))
    rootPackage
  }
}
