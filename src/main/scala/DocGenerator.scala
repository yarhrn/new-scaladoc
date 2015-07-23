import newmodel.Decl.Def
import newmodel.Defn.{Class, Object, Trait}
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

class LatexDocGenerator(index: Index) extends DocGenerator {


  def generateIndex(index: Index): String = {
    "\\newpage\n" +
      "Generated INDEX" + indexForObjects(index) + "\n" +
      "Generated INDEX" + indexForMethods(index) + "\n" +
      "Generated INDEX" + indexForClasses(index) + "\n" +
      "Generated INDEX" + indexForTraits(index) + "\n"
  }

  override def generate(root: Pkg) = {
    latexHeader + processDocTree(root) + generateIndex(index) + latexEnder
  }


  def processDocTree(root: Pkg): String = {
    extractAllPackages(root).map(processPackage).mkString("\n")
  }

  def extractAllPackages(root: Pkg) = {
    def loop(el: Pkg): Seq[Pkg] = el.stats.collect {
      case p: Pkg =>
        p +: loop(p)
    }.flatten
    loop(root) :+ root
  }

  def processPackage(pack: Pkg): String = {
    val grouped: Map[String, Seq[Tree]] = pack.stats.groupBy {
      case e: Class => "classes"
      case e: Object => "objects"
      case e: Trait => "traits"
      case e: Tree => "nvm"
    }
    val objects = pack.stats.collect { case o: Object => o }
    val traits = pack.stats.collect { case t: Trait => t }
    """\chapter{Package org}{""" +
      hypertarget(pack, None) + """}\hskip -.05in
         \hbox to \hsize{\textit{ Package Contents\hfil Page}}
         \vskip .13in
                                """ + processObjects(objects)
  }

  def processObjects(classes: Seq[Object]) = {
    "\\hbox{{\\bf  Objects}}" ++ classes.map(processObject).mkString("\n")
  }

  def processTraits(classes: Seq[Trait]) = {
    "\\hbox{{\\bf  Objects}}" ++ classes.map(processTrait).mkString("\n")
  }

  def dumpParent(tpe: Type.Name) = {
    val p = index.getByLink(tpe.id)
    val (name, tparams) = p match {
      case Some(o: Object) => (o.name, Seq())
      case Some(o: Trait) => (o.name, o.tparams)
      case Some(o: Class) => (o.name, o.tparams)
    }
    hyperlink(name, Some(name.name)) ++ dumpTypeParams(tparams)
  }

  def processTrait(trt: Trait): String = {
    val name = trt.name
    val comment = trt.comment.rawComment
    val methodsSummary = processMethodsSummary(trt.templ.stats)
    val methods = {
      trt.templ.stats.collect { case m: Def => processMethod(m) }.mkString("\n")
    }
    val mods = trt.mods.map(_.getClass.getSimpleName.toLowerCase).mkString(" ")
    val link = hypertarget(trt.name, Some(trt.name.name))
    val parent = trt.templ.parents.map(dumpParent).mkString(" with ")
    s"""
        \\entityintro{$name}{}{$comment}
        \\vskip .1in
        \\vskip .1in
        $link
        \\section{object $name}{
        \\vskip .1in
        $comment
        \\subsection{Declaration}{
        \\begin{lstlisting}[frame=none]
        $mods trait ${trt.name.name} extends $parent
        \\end{lstlisting}
        $methodsSummary
        \\subsection{Methods}{
        \\vskip -2em
        \\begin{itemize}
        $methods
        \\end{itemize}
        }}"""
  }

  def processObject(obj: Object): String = {
    val name = obj.name
    val comment = obj.comment.rawComment
    val methodsSummary = processMethodsSummary(obj.templ.stats)
    val methods =
      obj.templ.stats.collect { case m: Def => processMethod(m) }.mkString("\n")
    val mods = obj.mods.map(_.getClass.getSimpleName.toLowerCase).mkString(" ")
    val link = hypertarget(obj.name, Some(obj.name.name))
    s"""

        \\entityintro{$name}{}{$comment}
        \\vskip .1in
        \\vskip .1in
        $link
        \\section{object $name}{
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


  def processMethodsSummary(methods: Seq[Tree]): String = {
    s"""\\subsection{Method summary}{
        \\begin{verse}
          ${methods.collect { case e: Def => s"{\\bf def ${e.name}(${dumpMethodInputs(e)})}\\\\" }.mkString("\n")}
        \\end{verse}
        }"""
  }

  def dumpMethodInputs(e: Def): String = e.paramss.map(_.map(e => dumpType(e.decltpe)).mkString(", ")).mkString(")(")

  def commonIndex(elems: Seq[ {def name: String; def id: Seq[Tree]}]): String = {
    "\\begin{multicols}{2}\\noindent\n" +
      elems.map(e => s"{${hyperlink(e, Some(e.name))}\\\\}").mkString("\n") + "\n" +
      "\\end{multicols}"
  }

  def indexForMethods(index: Index): String = {
    commonIndex(index.defs)
  }

  def indexForObjects(index: Index): String = {
    commonIndex(index.objects.map(_.name))
  }

  def indexForClasses(index: Index): String = {
    commonIndex(index.classes.map(_.name))
  }

  def indexForTraits(index: Index): String = {
    commonIndex(index.traits.map(_.name))
  }


  def dumpSignature(e: Def) = {
    e.paramss.map(_.map(e => e.name + " : " + dumpType(e.decltpe)).mkString(", ")).mkString(")(")
  }

  def dumpTypeParams(tps: Seq[Type.Param]) = {
    val params = tps.map(dumpType).mkString(", ")
    if (params.nonEmpty) {
      s"[$params]"
    } else ""
  }

  def dumpType(tpe: Type): String = {
    tpe match {
      case e: Type.Name => hyperlink(e, Some(e.name))
      case e: Type.Apply => dumpType(e.tpe) + "[" + e.args.map(dumpType).mkString(", ") + "]"
      case e: Type.Compound => e.tpes.map(dumpType).mkString("with ")
      case e: Type.Param => {
        val name = dumpType(e.name)
        val tparams = dumpTypeParams(e.tparams)
        val lo = e.typeBounds.lo.map(" >: " + dumpType(_)).getOrElse("")
        val hi = e.typeBounds.hi.map(" <: " + dumpType(_)).getOrElse("")
        val ctx = e.contextBounds.map(dumpType).map(": " + _).mkString(" ")
        val view = e.viewBounds.map(dumpType).map("<% " + _).mkString(" ")
        "[" + name + tparams + hi + lo + ctx + view + "]"
      }
    }
  }

  def processMethod(m: Def): String = {
    val mods = m.mods.map(_.getClass.getSimpleName.toLowerCase.dropRight(1)).mkString(" ")
    val name = m.name
    val returnType = dumpType(m.decltpe)
    val comment = m.comment.rawComment
    val signature = dumpSignature(m)
    val tparams = dumpTypeParams(m.tparams)
    val methodRef = hypertarget(m, None)
    val linkId = link(m.id)
    s"""\\item{
        \\index{$linkId}
        {\\bf  $methodRef}
            $mods def $tparams($signature) : $returnType
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


  def hypertarget(e: {def id: Seq[Tree]}, text: Option[String]): String = {
    val t = text.getOrElse("")
    if (e.id.isEmpty)
      t
    else
      s"\\hypertarget{${link(e.id)}}{$t}"
  }

  def hyperlink(e: {def id: Seq[Tree]}, text: Option[String]) = {
    val t = text.getOrElse("")
    if (e.id.isEmpty)
      t
    else
      s"\\hyperlink{${link(e.id)}}{$t}"
  }

  def link(tpe: Seq[Tree]) = {
    val termToTerm = "."
    val termToType = "."
    val typeToType = "#"
    val typeToTerm = "#"
    def separtor(s: Tree, i: Int): String = if (s != tpe.last) {
      (s, tpe(i + 1)) match {
        case (e1: Term.Name, e2: Type.Name) => e1.name + termToType
        case (e1: Term.Name, e2: Term.Name) => e1.name + termToTerm
        case (e1: Type.Name, e2: Type.Name) => e1.name + typeToType
        case (e1: Type.Name, e2: Term.Name) => e1.name + typeToTerm
      }
    } else s match {
      case s: Type.Name => s.name
      case s: Term.Name => s.name
    }
    tpe.zipWithIndex.map {
      case (e, i) => separtor(e, i)
    }.mkString("")
  }


  def latexEnder: String = """\printindex
                      \end{document}"""
}
