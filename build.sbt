name := "new-scaladoc"
scalaVersion := "2.11.6"
resolvers += Resolver.sonatypeRepo("snapshots")
version := "1.0"

scalaVersion := "2.11.6"
libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
libraryDependencies += "org.scalameta" % "scalameta" % "0.1.0-SNAPSHOT" cross CrossVersion.binary

libraryDependencies += "org.scalameta" % "scalahost" % "0.1.0-SNAPSHOT" cross CrossVersion.full
libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.11.6"

libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"
resolvers += "Example Plugin Repository" at "https://example.org/repo/"

packAutoSettings

commands += Command.args("gendoc", "args") { (state, args) =>
  def getOsName =System.getProperty("os.name", "unknown")
  def isWindows = getOsName.toLowerCase.indexOf("windows") >= 0

  Project.runTask(pack, state)
  val List(src) = args
  if(isWindows){
    println("target\\pack\\bin\\main.bat" :: src :: Nil !!)
  }else{
    println("target/pack/bin/main" :: src :: Nil !!)
  }
  val pdfCmd = "pdflatex" :: "-interaction=nonstopmode" :: "doc.tex" :: Nil
  pdfCmd.run().exitValue()
  pdfCmd.run().exitValue()
  state
}
