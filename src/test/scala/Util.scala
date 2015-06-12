

import newmodel.Decl.Def
import newmodel.Defn.ObjectDoc
import newmodel.Param.ValueParam
import newmodel.Type.Name
import newmodel._

/**
 * Created by yaroslav on 09.06.2015.*/
object Util {
  def model1 = {
    val returnForMethod1 = Name("String")
    val fooMethod = Def("foo", returnForMethod1, Seq(ValueParam("a", returnForMethod1,
      Option.empty[Implicit], ID("testId"), Comment("cool method!"))), ID("org.bar.Test#foo"),Seq(), Comment("Cool method!"), Seq.empty[Flag])
    val fooMethod1 = Def("foo1", returnForMethod1, Seq(), ID("org.bar.Test#foo"),Seq(), Comment("Another cool method!"), Seq())
    val testObject = ObjectDoc("Test", Seq(fooMethod, fooMethod1), ID(""), Comment("Awesome test class with two methods"),Seq(), SourceFile("test1"))
    val barMethod = Def("bar", returnForMethod1, Seq(ValueParam("a", returnForMethod1, Option.empty[Implicit], ID("testId"),
      Comment("cool method!"))), ID("org.bar.Test#bar"),Seq(), Comment("Cool method! of bar"), Seq.empty[Flag])
    val bar1Method = Def("bar1", returnForMethod1, Seq(), ID("org.bar.Test#bar1"),Seq(), Comment("Another cool method of bar!"),Seq(Implicit()))
    val barObject = ObjectDoc("Bar", Seq(barMethod, bar1Method), ID(""), Comment("Awesome bar object with two methods"), Seq(),SourceFile("test"))
    val orgPackage = Package("org", List(testObject, barObject), ID("org"), Comment("Lorem ipsum!"))
    val rootPackage = Package("_root_", List(orgPackage), ID("_root_"), Comment(""))
    rootPackage
  }
}
