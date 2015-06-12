package newmodel

import newmodel.Ctor.ConstructorDoc
import newmodel.Param.ValueParam

case class ID(qualifiedName: String)

// for now
case class Comment(rawComment: String)

case class SourceFile(name: String)

//for now
trait DocElement {
  def id: ID

  def comment: Comment
}

case class Package(name: String,
                   elements: Seq[DocElement],
                   id: ID,
                   comment: Comment) extends DocElement

object Defn {


  case class ObjectDoc(name: String,
                       members: Seq[DocElement],
                       id: ID, comment: Comment,
                       flags: Seq[Flag],
                       file: SourceFile) extends DocElement

  case class TraitDoc(name: String,
                      members: Seq[DocElement],
                      id: ID,
                      comment: Comment,
                      flags: Seq[Flag],
                      file: SourceFile) extends DocElement

  case class ClassDoc(name: String,
                      members: Seq[DocElement],
                      primaryConstructor: Option[ConstructorDoc],
                      constructors: Seq[ConstructorDoc],
                      id: ID,
                      comment: Comment,
                      flags: Seq[Flag],
                      file: SourceFile,
                      companion: Option[ObjectDoc]) extends DocElement

}

object Ctor {

  //ConstructorDoc
  case class ConstructorDoc(name: String,
                            inputs: Seq[ValueParam],
                            id: ID,
                            comment: Comment) extends DocElement

}

object Decl {

  //ValDoc
  case class Val(name: String,
                 returnType: Type.Name,
                 id: ID,
                 comment: Comment,
                 flags: Seq[Flag]) extends DocElement

  //VarDoc
  case class Var(name: String,
                 returnType: Type.Name,
                 id: ID,
                 comment: Comment,
                 flags: Seq[Flag]) extends DocElement

  //MethodDoc
  case class Def(name: String,
                 returnType: Type.Name,
                 inputs: Seq[ValueParam],
                 id: ID,
                 tparams: Seq[Type.Param],
                 comment: Comment,
                 flags: Seq[Flag]) extends DocElement

}



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

}

object Param {

  case class ValueParam(name: String,
                        paramType: Type.Name,
                        implicitly: Option[Implicit],
                        id: ID,
                        comment: Comment) extends DocElement

}


case class ImplicitConversions(from: Type.Name,
                               to: Type.Name,
                               id: ID, comment: Comment) extends DocElement