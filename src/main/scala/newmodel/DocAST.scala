package newmodel
/**
 * Created by yaroslav on 11.05.2015.
 */


trait DocAST
case class Package(name: String, elements: Seq[DocAST]) extends DocAST

case class ClassDoc(name: String, members: Seq[DocAST]) extends DocAST

case class MethodDoc(name:String,returnType : Type,inputs:Option[Seq[ValueParam]]) extends DocAST

case class Type(name:String)

case class ValueParam(name:String,result: Type,isImplicit:Boolean) extends DocAST

case class ConstructorDoc(name:String,inputs:Option[Seq[ValueParam]]) extends DocAST

object Private extends Modifier
object Public extends Modifier
object Protected extends Modifier
object PackageProtected extends Modifier
trait Modifier{
  override def toString: String = this match {
    case Private => "private"
    case Public => "public"
    case Protected => "protected"
    case PackageProtected => "package"
  }
}