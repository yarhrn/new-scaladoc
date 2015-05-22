import newmodel._


trait DocGenerator {
  def generate(root: Package, index: Index): String
}

object PlainStringGenerator extends DocGenerator {
  override def generate(root: Package, index: Index): String = {
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
  override def generate(root: Package, index: Index) = {
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
    val grouped: Map[String, Seq[DocElement]] = pack.elements.groupBy {
      case e: ClassDoc => "classes"
      case e: ObjectDoc => "objects"
      case e: TraitDoc => "traits"
      case e: DocElement => "nvm"
    }
    processObjects(grouped("objects").map(_.asInstanceOf[ObjectDoc]))
  }

  def processObjects(classes: Seq[ObjectDoc]) = {
    """\chapter{Package org}{
      |\label{org}\hskip -.05in
      |\hbox to \hsize{\textit{ Package Contents\hfil Page}}
      |\vskip .13in
      |\hbox{{\bf  Objects}}
      |
      | """.stripMargin +
      classes.map(processObject).mkString("\n")
  }

  def processObject(obj: ObjectDoc): String = {
    s"\\entityintro{${obj.name}}{${obj.id.qualifiedName}_object}{${obj.comment.rawComment}}\n\\vskip .1in\n\\vskip .1in\n\\section{\\label{${obj.id.qualifiedName}_object}\\index{${obj.name}}object ${obj.name}}{\n\\vskip .1in \n${obj.comment.rawComment} " +
      s"\\subsection{Declaration}{\n\\begin{lstlisting}[frame=none]\nobject ${obj.name}\\end{lstlisting}" +
      processMethodsSummary(obj.members) +
      "\\subsection{Methods}{\n\\vskip -2em\n\\begin{itemize}" +
      obj.members.collect { case _: MethodDoc =>
        processMethod _
      }.mkString("\n") +
      "\n\\end{itemize}\n}" +
      "}"

  }

  def processMethodsSummary(mehtods: Seq[DocElement]): String = {
    "\\subsection{Method summary}{\n\\begin{verse}\n" + mehtods.collect {
      case e: MethodDoc =>
        s"{\\bf def ${e.name}(${dumpMethodInputs(e)})}\\\\"
    }.mkString("\n") + "\\end{verse}\n}"
  }

  def dumpMethodInputs(e: MethodDoc): String = e.inputs.map(_.paramType.name).mkString(",")

  def dumpSignature(e: MethodDoc) = e.inputs.map((input) => input.name + " : " + input.paramType.name).mkString(", ")

  def processMethod(m: MethodDoc): String = {
    s"\\item{ \n\\index{apply()}\n{\\bf  ${m.name}}\\\\\n\\begin{lstlisting}[frame=none]" +
      s"\ndef ${m.name}(${dumpSignature(m)}) : ${m.returnType.name}\\end{lstlisting} %end signature\n\\begin{itemize}\n\\item{" +
      s"\n{\\bf  Description}\n ${m.comment.rawComment}\n}\n\\end{itemize}\n}%end item"
  }


  def latexHeader = "\\documentclass[11pt,a4paper]{report}\n\\usepackage{color}\n\\usepackage{ifthen}\n\\usepackage{makeidx}\n\\usepackage{ifpdf}\n\\usepackage[headings]{fullpage}\n\\usepackage{listings}\n\\lstset{language=Java,breaklines=true}\n\\ifpdf \\usepackage[pdftex, pdfpagemode={UseOutlines},bookmarks,colorlinks,linkcolor={blue},plainpages=false,pdfpagelabels,citecolor={red},breaklinks=true]{hyperref}\n  \\usepackage[pdftex]{graphicx}\n  \\pdfcompresslevel=9\n  \\DeclareGraphicsRule{*}{mps}{*}{}\n\\else\n  \\usepackage[dvips]{graphicx}\n\\fi\n\n\\newcommand{\\entityintro}[3]{%\n  \\hbox to \\hsize{%\n    \\vbox{%\n      \\hbox to .2in{}%\n    }%\n    {\\bf  #1}%\n    \\dotfill\\pageref{#2}%\n  }\n  \\makebox[\\hsize]{%\n    \\parbox{.4in}{}%\n    \\parbox[l]{5in}{%\n      \\vspace{1mm}%\n      #3%\n      \\vspace{1mm}%\n    }%\n  }%\n}\n\\newcommand{\\refdefined}[1]{\n\\expandafter\\ifx\\csname r@#1\\endcsname\\relax\n\\relax\\else\n{$($in \\ref{#1}, page \\pageref{#1}$)$}\\fi}\n\\date{\\today}\n\\chardef\\textbackslash=`\\\\\n\\makeindex\n\\begin{document}\n\\sloppy\n\\addtocontents{toc}{\\protect\\markboth{Contents}{Contents}}\n\\tableofcontents\n"

  def latexEnder = "\\printindex\n\\end{document}\n"
}