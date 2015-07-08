package newmodel

import newmodel.Ctor.Constructor

// for now
case class Comment(rawComment: String)

case class Name(name: String, hash: Int)

case class SourceFile(name: String)

sealed trait Tree

sealed trait Stat extends Tree

//for now


sealed trait Defn extends Stat

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

sealed trait Ctor extends Stat

object Ctor {

  //ConstructorDoc
  case class Constructor(name: Name,
                         paramss: Seq[Seq[Term.Param]],
                         comment: Comment) extends Ctor

}

sealed trait Decl extends Stat

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
                 paramss: Seq[Seq[Term.Param]],
                 tparams: Seq[Type.Param],
                 comment: Comment,
                 mods: Seq[Mod]) extends Decl

}


sealed trait Type extends Tree

object Type {

  case class Name(name: String) extends Type


  case class Bounds(lo: Option[Type], hi: Option[Type])

  case class Param(mods: Seq[Mod],
                   name: Type.Name,
                   tparams: Seq[Type.Param],
                   typeBounds: Type.Bounds,
                   viewBounds: Seq[Type],
                   contextBounds: Seq[Type]) extends Type

  sealed trait Arg extends Tree


}


sealed trait Term

object Term {

  case class Param(name: String,
                   mods: Seq[Mod],
                   decltpe: Type) extends Term

  // todo add default val

}


sealed trait Mod extends Tree

object Mod {


  object Annot extends Mod

  object Private extends Mod

  // todo should be   class Private(within: Name.Qualifier)

  object Protected extends Mod

  object Implicit extends Mod

  object Final extends Mod

  object Sealed extends Mod

  object Override extends Mod

  object Case extends Mod

  object Abstract extends Mod

  object Covariant extends Mod

  object Contravariant extends Mod

  object Lazy extends Mod

  object ValParam extends Mod

  object VarParam extends Mod

  object Ffi extends Mod

}


