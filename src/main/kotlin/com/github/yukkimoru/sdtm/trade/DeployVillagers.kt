package com.github.yukkimoru.sdtm.trade

import com.github.yukkimoru.sdtm.trade.villagers.VillagerPickaxe
import com.github.yukkimoru.sdtm.trade.villagers.VillagerPotion
import com.github.yukkimoru.sdtm.trade.villagers.VillagerUtility
import com.github.yukkimoru.sdtm.trade.villagers.VillagerWeapon
import org.bukkit.Location
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class DeployVillagers(private val plugin: JavaPlugin) : Listener {
	// 特定の場所に村人を召喚する
	// VillagerWeaponクラスの村人を召喚

	fun summonVillagers() {
		VillagerWeapon(plugin).summonVillagerWeapon(
			Location(plugin.server.getWorld("world"), -992.5, 3.0, 1014.5),
			180f
		)

		VillagerPotion(plugin).summonVillagerPotion(
			Location(plugin.server.getWorld("world"), -990.5, 3.0, 1014.5),
			180f
		)

		VillagerPickaxe(plugin).summonVillagerPickaxe(
			Location(plugin.server.getWorld("world"), -988.5, 3.0, 1014.5),
			180f
		)

		VillagerUtility(plugin).summonVillagerUtility(
			Location(plugin.server.getWorld("world"), -986.5, 3.0, 1014.5),
			180f
		)
	}
}