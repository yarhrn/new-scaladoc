import newmodel._
import org.scalatest.FunSuite

class ModelTest extends FunSuite {

  def buildModel = {
    val returnForMethod1 = Type("String")
    val method = MethodDoc("foo", returnForMethod1, None)
    val constructor = ConstructorDoc("Test", Some(List.empty[ValueParam]))
    val classDoc = ClassDoc("Test", List(constructor, method))
    val barPackage = Package("bar", List(classDoc))
    val orgPackage = Package("org", List(barPackage))
    Package("_root_", List(orgPackage))
  }


  test("An empty Set should have size 0") {
    val rootPackage = buildModel
    println("Root package")
    def modelHandler(doc: DocAST): Unit = {
      def valParamsToStr(inputs: Option[Seq[ValueParam]]) = inputs match {
        case None => " "
        case Some(list) =>
          list.foldLeft("") {
            (str, valueParam: ValueParam) =>
              val implicitStr = if (valueParam.isImplicit) "implicit" else ""
              str + implicitStr + " " + valueParam.name + ": " + valueParam.result.name
          }
      }

      doc match {
        case Package(name, elems) =>
          println("Package name : " + name)
          elems.foreach(modelHandler)
        case ClassDoc(name, elems) =>
          println("Class name : " + name)
          elems.foreach(modelHandler)
        case MethodDoc(name, returnType, inputs) =>
          println(s"Method : def $name( ${valParamsToStr(inputs)} ): ${returnType.name}");
        case ConstructorDoc(name, inputs) =>
          println(s"Constructor : def $name(${valParamsToStr(inputs)})")


      }
    }
    modelHandler(rootPackage)
    assert(Set.empty.size == 0)
  }


}
