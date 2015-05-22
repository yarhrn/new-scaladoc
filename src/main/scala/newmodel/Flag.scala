package newmodel

/**
 * @author yaroslav.gryniuk
 */
sealed trait Flag

case class Sealed() extends Flag
case class Final() extends Flag
case class Implicit() extends Flag
case class Abstract() extends Flag