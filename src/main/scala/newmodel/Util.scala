package newmodel

/**
 * @author yaroslav.gryniuk
 */
object Util {
  def mod2str(mod: Mod) = mod.getClass.getSimpleName.toLowerCase
  def mods2str(mods:Seq[Mod]) = mods.map(mod2str).mkString(" ")
}
