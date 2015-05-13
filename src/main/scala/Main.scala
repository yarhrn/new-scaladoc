

import newmodel.Package
import newmodel._

import scala.collection.mutable.ListBuffer

/**
 * Created by yaroslav on 11.05.2015.
 */
object Main {

  object DummyIndex extends Index{
    val docElements = ListBuffer.empty[DocElement]
    override def getConversionsFor(id: ID): Seq[ImplicitConversions] = ???

    override def getKnownSubclasses(id: ID): Seq[ID] = ???

    override def getShadowedConversionsFor(id: ID): Seq[ImplicitConversions] = ???

    override def getLinearSupertypesBy(id: ID): Seq[ID] = ???

    override def getCompanionFor(id: ID): ID = ???

    override def getDocElementBy(id: ID): DocElement = docElements.find((x) => x.id == id).get

    def add(element:DocElement): Unit ={
      docElements += element
    }
  }
  def buildModel = {

    val returnForMethod1 = Type("String")
    val method = MethodDoc("foo", returnForMethod1, None,ID("org.bar.Test#foo"),Comment(""),false,false,false)
    val constructor = ConstructorDoc("Test", Some(List.empty[ValueParam]),ID("org.bar.Test#<init>"),Comment(""))
    val classDoc = ClassDoc("Test", List(constructor, method),ID("org.bar.Test"),Comment(""),false)
    val objectDoc = ObjectDoc("Test",Seq(),ID(""),Comment("org.bar.Test"))
    val barPackage = Package("bar", List(classDoc),ID("org.bar"),Comment(""))
    val orgPackage = Package("org", List(barPackage),ID("org"),Comment(""))
    val rootPackage = Package("_root_", List(orgPackage),ID("_root_"),Comment(""))

    import DummyIndex._
    add(method)
    add(constructor)
    add(classDoc)
    add(barPackage)
    add(orgPackage)
    add(rootPackage)

    rootPackage
  }


  def main(args:Array[String]):Unit = {
    val rootPackage = buildModel
    println("Root package")
    def modelHandler(doc: DocElement): String = {
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
        case Package(name, elems,_,_) =>
          "Package name : " + name + "\n" +  elems.map(modelHandler).mkString("\n")
        case ClassDoc(name, elems,_,_,_) =>
          "Class name : " + name + "\n" + elems.map(modelHandler).mkString("\n")
        case MethodDoc(name, returnType, inputs,_,_,_,_,_) =>
          s"Method : def $name( ${valParamsToStr(inputs)} ): ${returnType.name}"
        case ConstructorDoc(name, inputs,_,_) =>
          s"Constructor : def $name(${valParamsToStr(inputs)})"
      }
    }
    println(modelHandler(rootPackage))
  }

}


