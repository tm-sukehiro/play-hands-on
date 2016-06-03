package entities

object ItemCategory {
  case object Weapon extends ItemCategory
  case object Shield extends ItemCategory

  def unapply(category: ItemCategory): Option[String] = Some(category.name)
}
sealed abstract class ItemCategory {
  val name = this.toString
}