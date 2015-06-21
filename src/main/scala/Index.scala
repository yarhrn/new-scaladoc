


//trait Index {
//  def getConversionsFor(id: ID): Seq[ImplicitConversions]
//
//  def getShadowedConversionsFor(id: ID): Seq[ImplicitConversions]
//
//  def getCompanionFor(id: ID): Option[ID]
//
//  def getLinearSupertypesBy(id: ID): Seq[ID]
//
//  def getKnownSubclasses(id: ID): Seq[ID]
//
//
//}
//
//
//object Index {
//  def apply(docElements: Seq[DocElement]) = {
//    val companionMap = buildCompanionMap(docElements)
//
//
//    new Index {
//      override def getKnownSubclasses(id: ID): Seq[ID] = ???
//
//      override def getShadowedConversionsFor(id: ID): Seq[ImplicitConversions] = ???
//
//      override def getLinearSupertypesBy(id: ID): Seq[ID] = ???
//
//      override def getCompanionFor(id: ID): Option[ID] = companionMap.get(id).map(_.id)
//
//      override def getConversionsFor(id: ID): Seq[ImplicitConversions] = ???
//    }
//
//  }
//
//  def buildCompanionMap(docElements:Seq[DocElement]) ={
//    docElements.map {
//      case e: ClassDoc =>
//        docElements.find({
//          case ObjectDoc(`e`.name, _, _, _,_, `e`.file) => true
//        }).map(e.id -> _)
//      case _ => None
//    }.filter(_.isDefined).map(_.get).toMap
//  }
//}
