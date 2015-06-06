package newmodel

/**
 * @author yaroslav.gryniuk
 */
case class TypeParam(name: String,
                     variance: Option[Variance],
                     lowerBound: Option[Type], // >:
                     upperBound: Option[Type] // <:
                     )


sealed trait Variance

sealed case class Covariance() extends Variance

sealed case class Contravariance() extends Variance
