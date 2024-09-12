package com.github.YukkiMoru.SDTM.TRADE

import org.bukkit.Location
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class DeployVillagers(private val plugin: JavaPlugin) : Listener {
  // 特定の場所に村人を召喚する
  // VillagerWeaponクラスの村人を召喚

  fun summonVillagers() {
	VillagerWeapon(plugin).summonVillagerWeapon(Location(plugin.server.getWorld("world"), 95.5, 10.0, 90.5))

	VillagerPotion(plugin).summonVillagerPotion(Location(plugin.server.getWorld("world"), 97.5, 10.0, 90.5))

	VillagerPickaxe(plugin).summonVillagerPickaxe(Location(plugin.server.getWorld("world"), 99.5, 10.0, 90.5))
  }
}