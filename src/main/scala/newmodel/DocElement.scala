package newmodel

import newmodel.Ctor.ConstructorDoc
import newmodel.Param.ValueParam

case class ID(qualifiedName: String)

// for now
case class Comment(rawComment: String)

case class SourceFile(name: String)

trait Tree

//for now
trait DocElement {
  def id: ID

  def comment: Comment
}

case class Package(name: String,
                   elements: Seq[DocElement],
                   id: ID,
                   comment: Comment) extends DocElement


trait Defn extends DocElement

object Defn {


  case class ObjectDoc(name: String,
                       members: Seq[DocElement],
                       id: ID, comment: Comment,
                       flags: Seq[Mod],
                       file: SourceFile) extends Defn

  case class TraitDoc(name: String,
                      members: Seq[DocElement],
                      id: ID,
                      comment: Comment,
                      flags: Seq[Mod],
                      file: SourceFile) extends Defn

  case class ClassDoc(name: String,
                      members: Seq[DocElement],
                      primaryConstructor: Option[ConstructorDoc],
                      constructors: Seq[ConstructorDoc],
                      id: ID,
                      comment: Comment,
                      flags: Seq[Mod],
                      file: SourceFile,
                      companion: Option[ObjectDoc]) extends Defn

}

object Ctor {

  //ConstructorDoc
  case class ConstructorDoc(name: String,
                            inputs: Seq[Term.Param],
                            id: ID,
                            comment: Comment) extends DocElement

}

trait Decl extends DocElement

object Decl {

  //ValDoc
  case class Val(name: String,
                 returnType: Type.Name,
                 id: ID,
                 comment: Comment,
                 flags: Seq[Mod]) extends Decl

  //VarDoc
  case class Var(name: String,
                 returnType: Type.Name,
                 id: ID,
                 comment: Comment,
                 flags: Seq[Mod]) extends Decl

  //MethodDoc
  case class Def(name: String,
                 returnType: Type.Name,
                 inputs: Seq[Term.Param],
                 id: ID,
                 tparams: Seq[Type.Param],
                 comment: Comment,
                 flags: Seq[Mod]) extends Decl

}


trait Type

object Type {

  case class Name(name: String)

  sealed trait Variance

  sealed case class Covariance() extends Variance

  sealed case class Contravariance() extends Variance

  class Bounds(lo: Option[Name], hi: Option[Name])

  class Param(variance: Option[Variance],
              name: String,
              tparams: Seq[Type.Param],
              typeBounds: Type.Bounds,
              viewBounds: Seq[Name],
              contextBounds: Seq[Name])

  trait Arg extends Type.Arg with Tree


}



case class Name(name: String) extends ID(name)
trait Term
object Term {

  case class Param(mods: Seq[Mod],
                   name: Name
                   ) extends Term  // todo add default val

}


trait Mod extends Tree

object Mod {

  class Annot() extends Mod

  class Private() extends Mod

  // todo should be   class Private(within: Name.Qualifier)

  class Protected() extends Mod

  class Implicit() extends Mod

  class Final() extends Mod

  class Sealed() extends Mod

  class Override() extends Mod

  class Case() extends Mod

  class Abstract() extends Mod

  class Covariant() extends Mod

  class Contravariant() extends Mod

  class Lazy() extends Mod

  class ValParam() extends Mod

  class VarParam() extends Mod

  class Ffi(signature: String) extends Mod

}

