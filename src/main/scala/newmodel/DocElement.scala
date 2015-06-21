package newmodel

import newmodel.Ctor.Constructor

// for now
case class Comment(rawComment: String)

case class Name(name: String, hash: Int)

case class SourceFile(name: String)

trait Tree

trait Stat extends Tree

//for now


trait Defn extends Stat

object Defn {



  case class Object(name: Name,
                    templ: Template,
                    comment: Comment,
                    mods: Seq[Mod],
                    file: SourceFile) extends Defn

  case class Trait(name: Name,
                   templ: Template,
                   comment: Comment,
                   mods: Seq[Mod],
                   tparams: Seq[Type.Param],
                   file: SourceFile) extends Defn

  case class Class(name: Name,
                   ctor: Option[Constructor],
                   comment: Comment,
                   tparams: Seq[Type.Param],
                   mods: Seq[Mod],
                   file: SourceFile,
                   templ: Template,
                   companion: Option[Object]) extends Defn
}

case class Pkg(name: Name,
               stats: Seq[Stat],
               comment: Comment) extends Defn




case class Template(stats: Seq[Stat])

trait Ctor extends Stat

object Ctor {

  //ConstructorDoc
  case class Constructor(name: Name,
                            paramss: Seq[Term.Param],
                            comment: Comment) extends Ctor

}

trait Decl extends Stat

object Decl {

  //ValDoc
  case class Val(name: Name,
                 decltpe: Type,
                 comment: Comment,
                 mods: Seq[Mod]) extends Decl

  //VarDoc
  case class Var(name: Name,
                 decltpe: Type,
                 comment: Comment,
                 mods: Seq[Mod]) extends Decl

  //MethodDoc
  case class Def(name: Name,
                 decltpe: Type,
                 paramss: Seq[Term.Param],
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

  case class Bounds(lo: Option[Type], hi: Option[Type])

  case class Param(variance: Option[Variance],
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

