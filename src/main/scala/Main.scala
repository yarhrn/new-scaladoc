import java.io.{BufferedWriter, FileWriter, File}

import newmodel.{Comment, Pkg, Stat}

import scala.collection.immutable
import scala.collection.mutable.ListBuffer
import scala.meta.internal.ast
import scala.meta.internal.hosts.scalac.contexts.StandaloneContext

object Main {
  def main(args: Array[String]): Unit = {
    println("Start doc generating")
    println(args(0))
    try {
      val tex = ConverterExecutor.execute(args(0))
      val w = new BufferedWriter(new FileWriter("doc.tex"))
      w.write(tex)
      w.close()
    } catch {
      case e: Exception => println(e.getMessage)
    }
    println("End doc generating")
  }

  object ConverterExecutor {


    def execute(root: String): String = {
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
        loop(new File(root).listFiles()).map {
          a =>
            try
              Some(a.parse[Source])
            catch {
              case e: Exception => println(a); None
            }
        }

      val someSources: Seq[Source] = sources.collect { case Some(a) => a }

      import scala.meta.internal.ast._
      import scala.meta.{Template => _, Term => _, _}

      val classes = new scala.collection.mutable.ListBuffer[ast.Pkg]()


      someSources.foreach {
        case a: ast.Source => a.transform {
          case a: ast.Pkg =>
            classes += a
            a
        }
      }
      val pkgsWithStats = classes.map(retrivePkgAndSupportedStats).collect { case Some(a) => a }
      val converted: ListBuffer[(String, Seq[newmodel.Stat])] = pkgsWithStats.map {
        case (name, stats) =>
          (name, stats.collect {
            case a: Defn.Class => ScalametaConverter.convertClass(a)
            case a: Defn.Object => ScalametaConverter.convertObject(a)
            case a: Defn.Trait => ScalametaConverter.convertTrait(a)
          })
      }
      val hierarchy = buildHierarchy(converted)
      LatexDocGenerator(hierarchy)
    }

    def buildHierarchy(stats: Seq[(String, Seq[Stat])]) = {
      val pkgs: Seq[Stat] = stats.groupBy(_._1).map(e => (e._1, e._2.flatMap(_._2))).map(e => Pkg(e._1, e._2, Comment(""), Seq())).toList
      Pkg("_root_",
        pkgs,
        Comment(""),
        Seq())
    }

    def retrivePkgAndSupportedStats(src: ast.Pkg): Option[(String, Seq[ast.Stat])] = src match {
      case ast.Pkg(name, stats: immutable.Seq[ast.Stat]) =>
        Some((name.toString().trim, stats))
      case _ => None
    }


  }

}


