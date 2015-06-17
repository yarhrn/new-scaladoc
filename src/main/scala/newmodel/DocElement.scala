package newmodel

import newmodel.Ctor.ConstructorDoc


// for now
case class Comment(rawComment: String)

case class Name(name: String, hash: Int)

case class SourceFile(name: String)

trait Tree

//for now


trait Defn extends Tree

object Defn {

  case class Package(name: Name,
                     elements: Seq[Tree],
                     comment: Comment) extends Tree


  case class ObjectDoc(name: Name,
                       members: Seq[Tree],
                       comment: Comment,
                       mods: Seq[Mod],
                       file: SourceFile) extends Defn

  case class TraitDoc(name: Name,
                      members: Seq[Tree],
                      comment: Comment,
                      mods: Seq[Mod],
                      file: SourceFile) extends Defn

  case class ClassDoc(name: Name,
                      members: Seq[Tree],
                      primaryConstructor: Option[ConstructorDoc],
                      constructors: Seq[ConstructorDoc],
                      comment: Comment,
                      tparams: Seq[Type.Param],
                      mods: Seq[Mod],
                      file: SourceFile,
                      companion: Option[ObjectDoc]) extends Defn

}

trait Ctor extends Tree

object Ctor {

  //ConstructorDoc
  case class ConstructorDoc(name: Name,
                            inputs: Seq[Term.Param],
                            comment: Comment) extends Ctor

}

trait Decl extends Tree

object Decl {

  //ValDoc
  case class Val(name: Name,
                 tpe: Type,
                 comment: Comment,
                 mods: Seq[Mod]) extends Decl

  //VarDoc
  case class Var(name: Name,
                 tpe: Type,
                 comment: Comment,
                 mods: Seq[Mod]) extends Decl

  //MethodDoc
  case class Def(name: Name,
                 returnType: Type,
                 inputs: Seq[Term.Param],
                 tparams: Seq[Type.Param],
                 comment: Comment,
                 mods: Seq[Mod]) extends Decl

}


trait Type extends Tree

object Type {

  case class Name(name: String) extends Type

  sealed trait Variance

  sealed case class Covariance() extends Variance

  sealed case class Contravariance() extends Variance

  class Bounds(lo: Option[Type], hi: Option[Type])

  class Param(variance: Option[Variance],
              name: Type.Name,
              tparams: Seq[Type.Param],
              typeBounds: Type.Bounds,
              viewBounds: Seq[Type],
              contextBounds: Seq[Type]) extends Type

  trait Arg extends Tree


}


trait Term

object Term {

  case class Param(name: String,
                   mods: Seq[Mod],
                   decltpe: Type) extends Term

  // todo add default val

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

