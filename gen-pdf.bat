call sbt pack
call target\pack\bin\main.bat
call pdflatex -interaction=nonstopmode --aux-directory=temp doc.tex
call pdflatex -interaction=nonstopmode --aux-directory=temp doc.tex
