import newmodel.Decl.Def
import newmodel.Defn.{Class, Object, Trait}
import newmodel.Mod.Contravariant
import newmodel._


trait DocGenerator {
  def generate(root: Pkg): String
}

object PlainStringGenerator extends DocGenerator {
  override def generate(root: Pkg): String = {
    /* println("Root package")
     def modelHandler(doc: DocElement): String = {
       def valParamsToStr(inputs: Seq[ValueParam]) =
         inputs.foldLeft("") {
           (str, valueParam: ValueParam) =>
             val implicitStr = if (valueParam.isImplicit) "implicit" else ""
             str + implicitStr + " " + valueParam.name + ": " + valueParam.result.name

         }

       doc match {
         case Package(name, elems, _, _) =>
           "Package name : " + name + "\n" + elems.map(modelHandler).mkString("\n")
         case ClassDoc(name, elems, _, _, _) =>
           "Class name : " + name + "\n" + elems.map(modelHandler).mkString("\n")
         case MethodDoc(name, returnType, inputs, _, _, _, _, _) =>
           s"Method : def $name( ${valParamsToStr(inputs)} ): ${returnType.name}"
         case ConstructorDoc(name, inputs, _, _) =>
           s"Constructor : def $name(${valParamsToStr(inputs)})"
       }
     }
     modelHandler(root)*/
    ??? // todo adapt
  }
}

object LatexDocGenerator extends DocGenerator {


  def generateIndex(index: Index): String = {
    //      "\\newpage\n" + "Generated INDEX" + indexForObjects(index) + "\n"

    ""
  }

  def processDocTree(root: Pkg): String = {
    extractAllPackages(root).map(processPackage).mkString("\n")
  }

  def extractAllPackages(root: Pkg) = {
    def loop(el: Pkg): Seq[Pkg] = el.stats.collect {
      case p: Pkg =>
        p +: loop(p)
    }.flatten
    loop(root)
  }

  def processPackage(pack: Pkg): String = {
    val grouped: Map[String, Seq[Tree]] = pack.stats.groupBy {
      case e: Class => "classes"
      case e: Object => "objects"
      case e: Trait => "traits"
      case e: Tree => "nvm"
    }
    val objects = pack.stats.collect { case o: Object => o }
    """\chapter{Package org}{
         \label{""" + label(pack) + """}\hskip -.05in
         \hbox to \hsize{\textit{ Package Contents\hfil Page}}
         \vskip .13in
         \hbox{{\bf  Objects}}
                                    """ + processObjects(objects)
  }

  def processObjects(classes: Seq[Object]) = {
    classes.map(processObject).mkString("\n")
  }

  def processObject(obj: Object): String = {
    val mods = obj.mods.map(_.getClass.getSimpleName.toLowerCase.dropRight(1)).mkString(" ")
    val qualifiedName = obj.name
    val name = obj.name
    val comment = obj.comment.rawComment
    val methodsSummary = processMethodsSummary(obj.templ.stats)
    val methods = {
      obj.templ.stats.collect { case m: Def => processMethod(m) }.mkString("\n")
    }
    obj.templ.stats.collect { case m: Def => processMethod(m) }.mkString("\n")

    s"""\\entityintro{$name}{${qualifiedName}_object}{$comment}
        \\vskip .1in
        \\vskip .1in
        \\section{\\label{${label(obj)}}\\index{$name}object $name}{
        \\vskip .1in
        $comment
        \\subsection{Declaration}{
        \\begin{lstlisting}[frame=none]
        $mods object ${obj.name}
        \\end{lstlisting}
        $methodsSummary
        \\subsection{Methods}{
        \\vskip -2em
        \\begin{itemize}
        $methods
        \\end{itemize}
        }}"""
  }

  def dumpTypeParam(tp: Type.Param): String = {
    def process(tpe: Option[Type], bound: String) = tpe.collect {
      case e: Type.Name => bound + e
      case e: Type.Param => bound + dumpTypeParam(e)
    }.getOrElse("")

    import newmodel.Mod.Covariant

    tp.mods.map { case _@Covariant => "+"; case _@Contravariant => "-" case _ => "" }.mkString("") +
      tp.name.link.last.name + dumpTypeParams(tp.tparams) +
      process(tp.typeBounds.lo, " >: ") +
      process(tp.typeBounds.hi, " <: ") +
      tp.contextBounds.collect { case e: Type.Name => s": ${e.link.last.name}" }.mkString(" ") +
      tp.viewBounds.collect { case e: Type.Name => s"<% ${e.link.last.name}" }.mkString(" ")


  }

  def dumpTypeParams(tps: Seq[Type.Param]) = {
    val params = tps.map(dumpTypeParam).mkString(", ")
    if (params.nonEmpty) {
      s"[$params]"
    } else ""
  }

  def processMethodsSummary(methods: Seq[Tree]): String = {
    s"""\\subsection{Method summary}{
        \\begin{verse}
          ${methods.collect { case e: Def => s"{\\bf def ${e.name}(${dumpMethodInputs(e)})}\\\\" }.mkString("\n")}
        \\end{verse}
        }"""
  }

  def dumpMethodInputs(e: Def): String = e.paramss.map(_.map(e => e.decltpe.asInstanceOf[Type.Name].link.last.name).mkString(", ")).mkString(")(")

  //    def commonIndex(elems: Seq[ {def name: Type.Name}], link: (Type.Name => String)): String = {
  //      "\\begin{multicols}{2}\\noindent\n" +
  //        elems.map(e => s"{${e.name}\\ref{${link(e.name)}}\\\\}").mkString("\n") + "\n" +
  //        "\\end{multicols}"
  //    }
  //
  //    def indexForMethods(index: Index): String = {
  //      commonIndex(index.defs, e => e.name)
  //    }
  //
  //    def indexForObjects(index: Index): String = {
  //      commonIndex(index.objects, e => s"${e.link.last.name}_object")
  //    }
  //
  //    def indexForClasses(index: Index): String = {
  //      commonIndex(index.classes, e => s"${e.name}_class")
  //    }
  //
  //    def indexForTraits(index: Index): String = {
  //      commonIndex(index.traits, e => s"${e.name}_trait")
  //    }


//  def dumpSignature(e: Def) = e.paramss.map(_.map(e => e.name + " : " + e.decltpe.last.name).mkString(", ")).mkString(")(")

  def processMethod(m: Def): String = {
    val mods = m.mods.map(_.getClass.getSimpleName.toLowerCase.dropRight(1)).mkString(" ")
    val name = m.name
    val returnType = m.decltpe match {
      case e: Type.Name => e
      case _ => "ERROR" //todo add handling
    }
    val comment = m.comment.rawComment
    val signature = "Stub"//dumpSignature(m)
    val tparams = dumpTypeParams(m.tparams)
    s"""\\item{
        \\index{$name()}
        {\\bf  $name}
        \\begin{lstlisting}[frame=none]
          $mods def $tparams($signature) : $returnType\\end{lstlisting}
        \\begin{itemize}
        \\item{
        {\\bf  Description}
         $comment
        }
        \\end{itemize}}"""
  }


  def latexHeader = {
    val slash = '\\'
    val dollar = '$'
    s"""${slash}documentclass[11pt,a4paper]{report}
                       ${slash}usepackage{color}
                       ${slash}usepackage{ifthen}
                       ${slash}usepackage{makeidx}
                       ${slash}usepackage{ifpdf}
                       ${slash}usepackage[headings]{fullpage}
                       ${slash}usepackage{listings}
                       ${slash}usepackage{multicol}
                       \\lstset{language=Java,breaklines=true}
                       \\ifpdf ${slash}usepackage[pdftex, pdfpagemode={UseOutlines},bookmarks,colorlinks,linkcolor={blue},plainpages=false,pdfpagelabels,citecolor={red},breaklinks=true]{hyperref}
                         ${slash}usepackage[pdftex]{graphicx}
                         \\pdfcompresslevel=9
                         \\DeclareGraphicsRule{*}{mps}{*}{}
                       \\else
                         ${slash}usepackage[dvips]{graphicx}
                       \\fi

                      \\newcommand{\\entityintro}[3]{%
                         \\hbox to \\hsize{%
                           \\vbox{%
                             \\hbox to .2in{}%
                           }%
                           {\\bf  #1}%
                           \\dotfill\\pageref{#2}%
                         }
                         \\makebox[\\hsize]{%
                           \\parbox{.4in}{}%
                           \\parbox[l]{5in}{%
                             \\vspace{1mm}%
                             #3%
                             \\vspace{1mm}%
                           }%
                         }%
                       }
                       \\newcommand{\\refdefined}[1]{
                       \\expandafter\\ifx\\csname r@#1\\endcsname\\relax
                       \\relax\\else
                       {$dollar(${dollar}in \\ref{#1}, page \\pageref{#1}$dollar)$dollar}\\fi}
                       \\date{\\today}
                       \\chardef\\textbackslash=`\\\\
                       \\makeindex
                       \\begin{document}
                       \\sloppy
                       \\addtocontents{toc}{\\protect\\markboth{Contents}{Contents}}
                       \\tableofcontents
                    """
  }


  def label(e: {def id: Seq[Tree]}): String = {
    link(e.id)
  }

  def link(tpe: Seq[Tree]) = {
    val termToTerm = "."
    val termToType = "."
    val typeToType = "#"
    def separtor(s: Tree, i: Int) = if (s != tpe.last) (s, tpe(i + 1)) match {
      case (e1: Term.Name, e2: Type.Name) => e1.name + termToType
      case (e1: Term.Name, e2: Term.Name) => e1.name + termToTerm
      case (e1: Type.Name, e2: Type.Name) => e1.name + typeToType
    } else {
      case s: {def name: String} => s.name
    }
    tpe.zipWithIndex.map {
      case (e, i) => separtor(e, i)
    }.mkString("")
  }


  def latexEnder: String = """\printindex
                      \end{document}"""

  override def generate(root: Pkg): String = ""
}
