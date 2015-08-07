name := "new-scaladoc"

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
resolvers += "Example Plugin Repository" at "https://example.org/repo/"

packAutoSettings

commands += Command.args("gendoc", "args") { (state, args) =>
  Project.runTask(pack, state)
  val List(src, compiled) = args
  println("target\\pack\\bin\\main.bat" :: src :: compiled :: Nil !!)
  val pdfCmd = "pdflatex" :: "-interaction=nonstopmode" :: "--aux-directory=temp" :: "doc.tex" :: Nil
  pdfCmd run()
  pdfCmd run()
  state
}
