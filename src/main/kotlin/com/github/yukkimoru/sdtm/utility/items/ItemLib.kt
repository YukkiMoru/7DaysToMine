package com.github.yukkimoru.sdtm.utility.items

import com.github.yukkimoru.sdtm.utility.ItemFactory
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

@Suppress("unused")
class ItemLib(val plugin: JavaPlugin) {
	object Equip {
		const val BOOTS = "boots"
		const val HELMET = "helmet"
		const val CHESTPLATE = "chestplate"
		const val LEGGINGS = "leggings"
	}

	fun isWearingEquip(player: Player, equipment: String, customModelData: Int): Boolean {
		val item: ItemStack = when (equipment) {
			Equip.BOOTS -> player.inventory.boots
			Equip.HELMET -> player.inventory.helmet
			Equip.CHESTPLATE -> player.inventory.chestplate
			Equip.LEGGINGS -> player.inventory.leggings
			else -> throw IllegalArgumentException("Invalid equipment type: $equipment")
		} ?: return false
		return ItemFactory(plugin).isItemWithCustomModelData(item, customModelData)
	}

	fun delayTick(ticks: Long = 1L, task: () -> Unit) {
		object : BukkitRunnable() {
			override fun run() {
				task()
			}
		}.runTaskLater(plugin, ticks)
	}

	fun runWithCooldown(cooldown: Long, task: () -> Unit) {
		if (!isRunning) {
			isRunning = true
			delayTick(1L) {
				task()
				delayTick(cooldown) {
					isRunning = false
				}
			}
		}
	}

	companion object {
		private var isRunning = false
	}
}