package newmodel

/**
 * @author yaroslav.gryniuk
 */
case class TypeParam(name: String,
                     variance: Option[Variance],
                     lowerBound: Option[Type], // >:
                     upperBound: Option[Type], // <:
                     contextBound: Seq[Type],  // :
                     viewBound: Seq[Type])     // <%


sealed trait Variance

sealed case class Covariance()

sealed case class Contravariance()
