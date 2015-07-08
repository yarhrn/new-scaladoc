import scala.meta.{Term => _, Template => _, _}
import scala.meta.internal.ast._
import scala.meta.dialects.Scala211

object Test {
  def main(args: Array[String]): Unit = {
    val scalaLibraryJar = classOf[App].getProtectionDomain().getCodeSource()
    val scalaLibraryPath = scalaLibraryJar.getLocation().getFile()
    println(scalaLibraryPath)
    //
    val scalaLibrary = "C:/Users/yaroslav.gryniuk/.ivy2/cache/org.scala-lang/scala-library/jars/scala-library-2.11.6.jar"
    val echoClasses = "C:\\Users\\yaroslav.gryniuk\\Documents\\new-scaladoc\\target\\scala-2.11\\classes\\metaexample"
    val classpath = s"$scalaLibrary;$echoClasses"
    val sourcepath = "C:\\Users\\yaroslav.gryniuk\\Documents\\new-scaladoc\\src\\main\\scala\\metaexample"

    implicit val c = Scalahost.mkProjectContext(sourcepath, classpath)
    val List(echoScalaActors) = c.project.sources

  }
}