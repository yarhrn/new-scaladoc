import java.io.File

import newmodel.Defn.Trait
import newmodel.Term.Param
import newmodel._

import scala.collection.immutable
import scala.meta.internal.ast
import scala.meta.internal.hosts.scalac.contexts.StandaloneContext
import scala.meta.ui.Structure


object ScalametaConverter {

  val STUB_DECLTYPE = Type.Name("STUB", Seq())
  val STUB_COMMENT = newmodel.Comment("STUB")
  val STUB_SOURCEFILE = newmodel.SourceFile("STUB")

  def convert(root: ast.Pkg): Unit = {
    println(root.ref.asInstanceOf[ast.Term.Name].value)
  }


  def convertTypeName(tpe: ast.Type.Name) = Type.Name(tpe.value, Seq())

  def convertTermName(termName: ast.Term.Name) = Term.Name(termName.value, Seq())

  def convertDef(deff: ast.Defn.Def): Decl.Def = newmodel.Decl.Def(
    deff.name.value,
    STUB_DECLTYPE,
    convertSeqSeqParams(deff.paramss),
    convertTypeParams(deff.tparams),
    STUB_COMMENT,
    convertMods(deff.mods),
    Seq())

  def convertTrait(trt: ast.Defn.Trait): Trait = Trait(convertTypeName(trt.name), convertTemplate(trt.templ), STUB_COMMENT, convertMods(trt.mods), convertTypeParams(trt.tparams), STUB_SOURCEFILE)

  def convertClass(trt: ast.Defn.Class): newmodel.Defn.Class = newmodel.Defn.Class(convertTypeName(trt.name), None, STUB_COMMENT, convertTypeParams(trt.tparams), convertMods(trt.mods), STUB_SOURCEFILE, convertTemplate(trt.templ), None)

  def convertObject(trt: ast.Defn.Object): newmodel.Defn.Object = newmodel.Defn.Object(convertTermName(trt.name), convertTemplate(trt.templ), STUB_COMMENT, convertMods(trt.mods), STUB_SOURCEFILE)

  def convertStats(stats: Option[immutable.Seq[ast.Stat]]): Seq[newmodel.Stat] = stats.map(_.collect(convertStat)).getOrElse(Seq())

  def convertDeclType(d: ast.Decl.Type): Decl.Type = Decl.Type(convertMods(d.mods), convertTypeName(d.name), convertTypeParams(d.tparams), convertTypeBounds(d.bounds))

  val convertStat: PartialFunction[ast.Stat, Stat] = {
    case e: ast.Defn.Class => convertClass(e)
    case d: ast.Defn.Trait => convertTrait(d)
    case d: ast.Defn.Object => convertObject(d)
    case d: ast.Defn.Def => convertDef(d)
    case d: ast.Decl.Type => convertDeclType(d)
  }


  def convertTemplate(tmplt: ast.Template): Template = Template(Seq(), convertStats(tmplt.stats))

  def convertSeqSeqParams(input: Seq[Seq[ast.Term.Param]])
  : Seq[Seq[Param]] = input.map(_.map(convertTermParam))

  def convertTermParam(termParam: ast.Term.Param): Param = newmodel.Term.Param(termParam.name.value, convertMods(termParam.mods), STUB_DECLTYPE)

  def convertTypeParam(tpe: ast.Type.Param): Type.Param =
    Type.Param(
      convertMods(tpe.mods),
      convert(tpe.name),
      convertTypeParams(tpe.tparams),
      convertTypeBounds(tpe.typeBounds),
      convertTypes(tpe.viewBounds),
      convertTypes(tpe.contextBounds)
    )

  def convertTypes(tpes: Seq[ast.Type]): Seq[Type] = tpes.map(convertType)

  def convertTypeBounds(tBounds: ast.Type.Bounds): Type.Bounds = Type.Bounds(tBounds.lo.map(convertType), tBounds.hi.map(convertType))

  def convertTypeParams(tparams: Seq[ast.Type.Param]): Seq[Type.Param] = tparams.map(convertTypeParam)

  def convert(tpn: ast.Type.Param.Name): Type = ???

  def convertMods(mods: Seq[ast.Mod])(implicit d: DummyImplicit, z: DummyImplicit) = Seq()

  val convertType: PartialFunction[ast.Type, Type] = {
    case t: ast.Type.Name => convert(t)
    case t: ast.Type.Param => convertTypeParam(t)
  }


}

object ConverterExecutor {
  def main(args: Array[String]): Unit = {
    val scalaLibraryJar = classOf[App].getProtectionDomain().getCodeSource()
    val scalaLibraryPath = scalaLibraryJar.getLocation().getFile()
    import scala.meta._
    import scala.meta.dialects.Scala211
    implicit val c: StandaloneContext = Scalahost.mkStandaloneContext(s"-cp $scalaLibraryPath")


    def loop(files: Seq[File]): Seq[File] = if (files.nonEmpty) {
      files.filter(_.getName.endsWith(".scala")) ++ files.filter(e => e.isDirectory).flatMap(e => loop(e.listFiles))
    } else {
      Seq()
    }

    val sources: Seq[Option[Source]] =
      loop(new File("C:\\Users\\yaroslav\\Documents\\new-scaladoc\\src\\main\\scala").listFiles()).map {
        a =>
          try
            Some(a.parse[Source])
          catch {
            case e: Exception => println(a); None
          }
      }

    val someSources: Seq[Source] = sources.collect { case Some(a) => a }

    //println(ScalametaConverter.convert(someSources.last.asInstanceOf[ast.Source].stats(0).asInstanceOf[ast.Pkg]))
    println(someSources.last.asInstanceOf[ast.Source].show[Structure])


    val a: Source = new java.io.File("C:\\Users\\yaroslav\\Documents\\new-scaladoc\\src\\main\\scala\\test\\Test.scala").parse[Source]
    val tokens: Tokens = a.asInstanceOf[ast.Source].stats(0).asInstanceOf[ast.Pkg].tokens
  }


}
