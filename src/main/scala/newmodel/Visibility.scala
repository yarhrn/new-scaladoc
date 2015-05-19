package newmodel

sealed trait Visibility
// private[this]
case class PrivateInInstance() extends Visibility
// protected[this]
case class ProtectedInInstance() extends Visibility
// private[owner]
case class PrivateInOwner(owner: DocElement) extends Visibility

// protected[owner]
case class ProtectedInOwner(owner: DocElement) extends Visibility

case class Private() extends Visibility
case class Protected() extends Visibility
