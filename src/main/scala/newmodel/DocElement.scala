package newmodel

/**
 * Created by yaroslav on 11.05.2015.
 */

case class ID(qualifiedName: String)

// for now
case class Comment(rawComment: String)

//for now
trait DocElement {
  def id: ID

  def comment: Comment
}

// Index concept here
trait Index {
  def getConversionsFor(id: ID): Seq[ImplicitConversions]

  def getShadowedConversionsFor(id: ID): Seq[ImplicitConversions]

  def getCompanionFor(id: ID): ID

  def getDocElementBy(id: ID): DocElement

  def getLinearSupertypesBy(id: ID): Seq[ID]

  def getKnownSubclasses(id: ID): Seq[ID]

}

case class Package(name: String, elements: Seq[DocElement], id: ID, comment: Comment) extends DocElement

case class ValueParam(name: String, result: Type, isImplicit: Boolean, id: ID, comment: Comment) extends DocElement

case class ConstructorDoc(name: String, inputs: Option[Seq[ValueParam]], id: ID, comment: Comment) extends DocElement

case class MethodDoc(name: String, returnType: Type, inputs: Option[Seq[ValueParam]], id: ID,
                     comment: Comment, isFinal: Boolean, isAbstract: Boolean, isOverride: Boolean) extends DocElement

case class ObjectDoc(name:String,members:Seq[DocElement],id:ID,comment: Comment) extends DocElement

case class TraitDoc(name:String,members:Seq[DocElement],id:ID,comment: Comment) extends DocElement

case class ClassDoc(name: String, members: Seq[DocElement], id: ID, comment: Comment, isCaseClass:Boolean) extends DocElement

case class Type(name: String)

case class ImplicitConversions(from: Type, to: Type,id:ID,comment: Comment) extends DocElement

object Private extends Modifier

object Public extends Modifier

object Protected extends Modifier

object PackageProtected extends Modifier

trait Modifier {
  override def toString: String = this match {
    case Private => "private"
    case Public => "public"
    case Protected => "protected"
    case PackageProtected => "package"
  }
}

