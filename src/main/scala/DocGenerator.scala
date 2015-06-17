import newmodel.Decl.Def
import newmodel.Defn.{TraitDoc, ObjectDoc, ClassDoc,Package}
import newmodel._


trait DocGenerator {
  def generate(root: Package): String
}

object PlainStringGenerator extends DocGenerator {
  override def generate(root: Package): String = {
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
  override def generate(root: Package) = {
    latexHeader + processDocTree(root) + latexEnder
  }

  def processDocTree(root: Package): String = {
    extractAllPackages(root).map(processPackage).mkString("\n")
  }

  def extractAllPackages(root: Package) = {
    def loop(el: Package): Seq[Package] = el.elements.collect {
      case p: Package =>
        p +: loop(p)
    }.flatten
    loop(root)
  }

  def processPackage(pack: Package): String = {
    val grouped: Map[String, Seq[Tree]] = pack.elements.groupBy {
      case e: ClassDoc => "classes"
      case e: ObjectDoc => "objects"
      case e: TraitDoc => "traits"
      case e: Tree => "nvm"
    }
    val objects = pack.elements.collect { case o: ObjectDoc => o }
    processObjects(objects)
  }

  def processObjects(classes: Seq[ObjectDoc]) = {
    """\chapter{Package org}{
       \label{org}\hskip -.05in
       \hbox to \hsize{\textit{ Package Contents\hfil Page}}
       \vskip .13in
       \hbox{{\bf  Objects}}
    """ +
      classes.map(processObject).mkString("\n")
  }

  def processObject(obj: ObjectDoc): String = {
    val qualifiedName = obj.name.name
    val name = obj.name
    val comment = obj.comment
    val methodsSummary = processMethodsSummary(obj.members)
    val methods = {obj.members.collect { case m: Def => processMethod(m) }.mkString("\n")}
    s"""\\entityintro{$name}{${qualifiedName}_object}{$comment}
      \\vskip .1in
      \\vskip .1in
      \\section{\\label{${qualifiedName}_object}\\index{$name}object $name}{
      \\vskip .1in
      $comment
      \\subsection{Declaration}{
      \\begin{lstlisting}[frame=none]
      object ${obj.name}
      \\end{lstlisting}
      $methodsSummary
      \\subsection{Methods}{
      \\vskip -2em
      \\begin{itemize}
      $methods
      \\end{itemize}
      }}""" 
  }

  def processMethodsSummary(mehtods: Seq[Tree]): String = {
    s"""\\subsection{Method summary}{
      \\begin{verse}
        ${mehtods.collect { case e: Def => s"{\\bf def ${e.name}(${dumpMethodInputs(e)})}\\\\" }.mkString("\n")}
      \\end{verse}
      }""" 
  }

  def dumpMethodInputs(e: Def): String = e.inputs.map(_.decltpe.asInstanceOf[Type.Name]).mkString(",")

  def dumpSignature(e: Def) = e.inputs.map((input) => input.name + " : " + input.decltpe.asInstanceOf[Type.Name]).mkString(", ")

  def processMethod(m: Def): String = {
    val name = m.name
    val returnType = m.returnType
    val comment = m.comment.rawComment
    val signature = dumpSignature(m)
    s"""\\item{
      \\index{$name()}
      {\\bf  $name}
      \\begin{lstlisting}[frame=none]
        def $signature($signature) : $returnType\\end{lstlisting}
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
                    """}

  def latexEnder: String = """\printindex
                      \end{document}"""
}