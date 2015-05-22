package newmodel

case class ID(qualifiedName: String)

// for now
case class Comment(rawComment: String)

case class SourceFile(name: String)

//for now
trait DocElement {
  def id: ID

  def comment: Comment
}

case class Package(name: String,
                   elements: Seq[DocElement],
                   id: ID,
                   comment: Comment) extends DocElement

case class ValueParam(name: String,
                      paramType: Type,
                      implicitly: Option[Implicit],
                      id: ID,
                      comment: Comment) extends DocElement

case class ConstructorDoc(name: String,
                          inputs: Seq[ValueParam],
                          id: ID,
                          comment: Comment) extends DocElement

case class MethodDoc(name: String,
                     returnType: Type,
                     inputs: Seq[ValueParam],
                     id: ID,
                     comment: Comment,
                     flags: Seq[Flag]) extends DocElement

case class ObjectDoc(name: String,
                     members: Seq[DocElement],
                     id: ID, comment: Comment,
                     file: SourceFile) extends DocElement

case class TraitDoc(name: String,
                    members: Seq[DocElement],
                    id: ID,
                    comment: Comment,
                    file: SourceFile) extends DocElement

case class ClassDoc(name: String,
                    members: Seq[DocElement],
                    primaryConstructor: Option[ConstructorDoc],
                    constructors: Seq[ConstructorDoc],
                    id: ID,
                    comment: Comment,
                    isCaseClass: Boolean,
                    file: SourceFile,
                    companion:Option[ObjectDoc]) extends DocElement

case class Type(name: String)

case class ImplicitConversions(from: Type,
                               to: Type,
                               id: ID, comment: Comment) extends DocElement