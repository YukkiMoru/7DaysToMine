package com.github.yukkimoru.sdtm.utility.items

import com.github.yukkimoru.sdtm.utility.ItemFactory
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

@Suppress("unused")
class ItemLibrary(val plugin: JavaPlugin) {
	fun isWearingEquip(player: Player, equipment: String, customModelData: Int): Boolean {
		val item: ItemStack? = when (equipment) {
			"boots" -> player.inventory.boots
			"helmet" -> player.inventory.helmet
			"chestplate" -> player.inventory.chestplate
			"leggings" -> player.inventory.leggings
			else -> null
		}
		return item?.let { ItemFactory(plugin).isItemWithCustomModelData(it, customModelData) } == true
	}

	fun delay(delay: Long = 1L, task: () -> Unit) {
		object : BukkitRunnable() {
			override fun run() {
				task()
			}
		}.runTaskLater(plugin, delay)
	}
}