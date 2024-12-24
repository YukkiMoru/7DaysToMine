package com.github.yukkimoru.sdtm.trade

import com.github.yukkimoru.sdtm.trade.pickaxe.VillagerPickaxe
import com.github.yukkimoru.sdtm.trade.potion.VillagerPotion
import com.github.yukkimoru.sdtm.trade.utility.VillagerUtility
import com.github.yukkimoru.sdtm.trade.weapon.VillagerWeapon
import org.bukkit.Location
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class DeployVillagers(private val plugin: JavaPlugin) : Listener {
	// 特定の場所に村人を召喚する
	// VillagerWeaponクラスの村人を召喚

	fun summonVillagers() {
		VillagerWeapon(plugin).summonVillagerWeapon(
			Location(plugin.server.getWorld("world"), 1002.5, 100.0, -11.5),
			0f
		)

		VillagerPotion(plugin).summonVillagerPotion(
			Location(plugin.server.getWorld("world"), 1000.5, 100.0, -11.5),
			0f
		)

		VillagerPickaxe(plugin).summonVillagerPickaxe(
			Location(plugin.server.getWorld("world"), 998.5, 100.0, -11.5),
			0f
		)

		VillagerUtility(plugin).summonVillagerUtility(
			Location(plugin.server.getWorld("world"), 996.5, 100.0, -11.5),
			0f
		)
	}
}