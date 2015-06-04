package newmodel

/**
 * @author yaroslav.gryniuk
 */
case class TypeParam(name: String,
                     variance: Option[Variance],
                     lowerBound: Seq[TypeParam],     // >:
                     upperBound: Seq[TypeParam],     // <:
                     contextBound: Seq[TypeParam],     // :
                     viewBound: Seq[TypeParam])     // <%


sealed trait Variance

sealed case class Covariance()

sealed case class Contravariance()