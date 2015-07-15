import newmodel.Type
import org.scalatest.FunSuite


class LatexDocGeneratorTest extends FunSuite {
  lazy val model1Sample: String = io.Source.fromFile("src/test/resources/util.model1.latex").mkString
  test("basic test for latex generation") {
    //    assert(model1Sample == LatexDocGenerator.generate(Util.model1))
    val stringTypeName = Type.Name(Seq(Type.Term("java"), Type.Term("lang"), Type.Typee("String"), Type.Typee("InnerString")))
    println(LatexDocGenerator.link(stringTypeName))
  }

}
