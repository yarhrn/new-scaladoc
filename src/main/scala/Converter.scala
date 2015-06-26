import scala.meta.dialects.Scala211
import scala.meta.{Scalahost, _}

object Runtime extends App {
  val scalaLibraryJar = classOf[App].getProtectionDomain().getCodeSource()
  val scalaLibraryPath = scalaLibraryJar.getLocation().getFile()
  implicit val c = Scalahost.mkStandaloneContext(s"-cp $scalaLibraryPath")

  class foo

  val z =
    q"""object z{
          val z = 12
          def a = {
            val k = 22
            k
          }
        }
     """


  import scala.meta.internal.ast._


  def convertTemplate(template: Template): newmodel.Template = {
    newmodel.Template(template.stats.map(_.collect { case e: Defn.Def => convertDef(e) }))
  }

  def convertObject(obj: Defn.Object) = {
    newmodel.Defn.Object(newmodel.Name(obj.name.value, 12), convertTemplate(obj.templ), null, convertMod(obj.mods), null)
  }

  def convertDef(deff: Defn.Def): newmodel.Decl.Def = {
    newmodel.Decl.Def(newmodel.Name(
      deff.name.value, 1),
      deff.decltpe.map(convertType),
      deff.paramss.map(_.map(p => convertParam(p))),
      deff.tparams.map(convertTParam),
      null, // todo add comment support
      convertMod(deff.mods))
  }

  def convertParam(param: Term.Param): newmodel.Term.Param = {
    newmodel.Term.Param(param.name.value, convertMod(param.mods), Some(newmodel.Type.Name("STUB")))
  }

  def convertMod(mods: Seq[Mod]): Seq[newmodel.Mod] = Seq(newmodel.Mod.Abstract())

  def convertBounds(bounds: Type.Bounds): newmodel.Type.Bounds = {
    newmodel.Type.Bounds(bounds.hi.map(convertType), bounds.lo.map(convertType))
  }

  def convertTParam(param: Type.Param): newmodel.Type.Param = {
    newmodel.Type.Param(
      convertMod(param.mods),
      newmodel.Type.Name(param.name.value),
      param.tparams.map(convertTParam),
      convertBounds(param.typeBounds),
      param.viewBounds.map(convertType), param.contextBounds.map(convertType))
  }

  def convertType(tpe: Type): newmodel.Type = {
    tpe match {
      case e: Type.Name => newmodel.Type.Name(e.value)
      case param: Type.Param => convertTParam(param)
    }
  }

}