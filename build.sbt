name := "new-scaladoc"
scalaVersion := "2.11.6"
resolvers += Resolver.sonatypeRepo("snapshots")

version := "1.0"

scalaVersion := "2.11.6"

libraryDependencies += "org.scalatest" % "scalatest_2.11" % "2.2.4" % "test"
libraryDependencies += "org.scalameta" % "scalameta" % "0.1.0-SNAPSHOT" cross CrossVersion.binary

libraryDependencies += "org.scalameta" % "scalahost" % "0.1.0-SNAPSHOT" cross CrossVersion.full
libraryDependencies += "org.scala-lang" % "scala-compiler" % "2.11.6"

