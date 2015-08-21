import newmodel.Defn.Trait
import newmodel.Term.Param
import newmodel._

import scala.collection.immutable
import scala.meta.internal.ast
import scala.meta.internal.ast.Type.Apply


object ScalametaConverter {

  val STUB_DECLTYPE = Type.Name("STUB", Seq())
  val STUB_COMMENT = newmodel.Comment("STUB")
  val STUB_SOURCEFILE = newmodel.SourceFile("STUB")

  def convert(root: ast.Pkg): Unit = {
    println(root.ref.asInstanceOf[ast.Term.Name].value)
  }


  def convertTypeName(tpe: ast.Type.Name) = Type.Name(tpe.value, Seq())

  def convertTypeName(tpe: ast.Type.Name, id: Seq[Tree]) = Type.Name(tpe.value, id)

  def convertTermName(termName: ast.Term.Name) = Term.Name(termName.value, Seq())

  def convertTermName(termName: ast.Term.Name, id: Seq[Tree]) = Term.Name(termName.value, Seq())

  def convertDef(deff: ast.Defn.Def): Decl.Def = newmodel.Decl.Def(
    deff.name.value,
    deff.decltpe.map(convertType).getOrElse(STUB_DECLTYPE),
    convertSeqSeqParams(deff.paramss),
    convertTypeParams(deff.tparams),
    STUB_COMMENT,
    convertMods(deff.mods),
    buildLink(deff))

  def convertDef(deff: ast.Decl.Def): Decl.Def = newmodel.Decl.Def(
    deff.name.value,
    convertType(deff.decltpe),
    convertSeqSeqParams(deff.paramss),
    convertTypeParams(deff.tparams),
    STUB_COMMENT,
    convertMods(deff.mods),
    buildLink(deff))

  def convertTrait(trt: ast.Defn.Trait): Trait =
    Trait(convertTypeName(trt.name, buildLink(trt)),
      convertTemplate(trt.templ),
      STUB_COMMENT,
      convertMods(trt.mods),
      convertTypeParams(trt.tparams),
      STUB_SOURCEFILE)

  def convertClass(trt: ast.Defn.Class): newmodel.Defn.Class =
    newmodel.Defn.Class(convertTypeName(trt.name, buildLink(trt)),
      None,
      STUB_COMMENT,
      convertTypeParams(trt.tparams),
      convertMods(trt.mods),
      STUB_SOURCEFILE,
      convertTemplate(trt.templ),
      None)

  def convertObject(trt: ast.Defn.Object): newmodel.Defn.Object =
    newmodel.Defn.Object(convertTermName(trt.name, buildLink(trt)),
      convertTemplate(trt.templ),
      STUB_COMMENT,
      convertMods(trt.mods),
      STUB_SOURCEFILE)

  def convertStats(stats: Option[immutable.Seq[ast.Stat]]): Seq[newmodel.Stat] = stats.map(_.collect(convertStat)).getOrElse(Seq())

  def convertDeclType(d: ast.Decl.Type): Decl.Type = Decl.Type(convertMods(d.mods), convertTypeName(d.name), convertTypeParams(d.tparams), convertTypeBounds(d.bounds))

  def convertDefnType(d: ast.Defn.Type): Defn.Type= Defn.Type(convertMods(d.mods),convertTypeName(d.name),convertTypeParams(d.tparams),convertType(d.body))


  val convertStat: PartialFunction[ast.Stat, Stat] = {
    case e: ast.Defn.Class => convertClass(e)
    case d: ast.Defn.Trait => convertTrait(d)
    case d: ast.Defn.Object => convertObject(d)
    case d: ast.Defn.Def => convertDef(d)
    case d: ast.Decl.Def => convertDef(d)
    case d: ast.Decl.Type => convertDeclType(d)
    case d: ast.Defn.Type => convertDefnType(d)
  }


  def convertTemplate(tmplt: ast.Template): Template = Template(Seq(), convertStats(tmplt.stats))

  def convertSeqSeqParams(input: Seq[Seq[ast.Term.Param]])
  : Seq[Seq[Param]] = input.map(_.map(convertTermParam))

  def convertTermParam(termParam: ast.Term.Param): Param = newmodel.Term.Param(termParam.name.value, convertMods(termParam.mods), STUB_DECLTYPE)

  def convertTypeParam(tpe: ast.Type.Param): Type.Param =
    Type.Param(
      convertMods(tpe.mods),
      convertTypeParamName(tpe.name),
      convertTypeParams(tpe.tparams),
      convertTypeBounds(tpe.typeBounds),
      convertTypes(tpe.viewBounds),
      convertTypes(tpe.contextBounds)
    )

  def convertTypes(tpes: Seq[ast.Type]): Seq[Type] = tpes.map(convertType)

  def convertTypeBounds(tBounds: ast.Type.Bounds): Type.Bounds = Type.Bounds(tBounds.lo.map(convertType), tBounds.hi.map(convertType))

  def convertTypeParams(tparams: Seq[ast.Type.Param]): Seq[Type.Param] = tparams.map(convertTypeParam)

  def convertTypeParamName(tpn: ast.Type.Param.Name): Type = Type.Name(tpn.toString().trim,Seq())

  def convertMods(mods: Seq[ast.Mod]) = Seq()

  def convertTypeApply(t: Apply): Type = Type.Apply(convertType(t.tpe), convertTypes(t.args))

  val convertType: PartialFunction[ast.Type, Type] = {
    case t: ast.Type.Name => convertTypeName(t)
    case t: ast.Type.Param => convertTypeParam(t)
    case t: ast.Type.Apply => convertTypeApply(t)
  }


  def buildLink(tree: scala.meta.Tree): Seq[Tree] = {
    val name = tree match {
      case e: ast.Defn.Class => Some(Type.Name(e.name.value, Seq()))
      case e: ast.Defn.Trait => Some(Type.Name(e.name.value, Seq()))
      case e: ast.Defn.Object => Some(Term.Name(e.name.value, Seq()))
      case e: ast.Pkg => Some(Term.Name(e.ref.toString().trim, Seq()))
      case e: ast.Defn.Def => Some(Term.Name(e.name.value, Seq()))
      case _ => None
    }
    val parent = tree.parent
    name.flatMap { n =>
      parent.map { p =>
        buildLink(p) :+ n
      }
    }.getOrElse {
      parent.map(buildLink).getOrElse(Seq())
    }
  }
}


