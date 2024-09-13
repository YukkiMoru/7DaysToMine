package com.github.YukkiMoru.SDTM.WORLD

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

class DropOres(private val plugin: JavaPlugin) : Listener {

	@EventHandler
	fun onBlockBreak(event: BlockBreakEvent) {
		val block = event.block
		val player = event.player
		val itemInHand: ItemStack = player.inventory.itemInMainHand

		if (block.type == Material.COAL_ORE || block.type == Material.IRON_ORE || block.type == Material.DEEPSLATE_IRON_ORE) {
			event.isDropItems = false // Prevent default drops

			val key = NamespacedKey(plugin, "destroyable_blocks")
			val luck = getLuckParameter(itemInHand, key) ?: 1.0

			// message to player
			player.sendMessage("Luck: $luck")

			val dropCount = calculateDropCount(luck)
			val dropItem = when (block.type) {
				Material.COAL_ORE -> Material.COAL
				Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE -> Material.RAW_IRON
				else -> return
			}

			block.world.dropItemNaturally(block.location, ItemStack(dropItem, dropCount))
		}
	}

	private fun getLuckParameter(item: ItemStack, key: NamespacedKey): Double? {
		if (item.type == Material.IRON_PICKAXE && item.itemMeta?.isUnbreakable == true) {
			val container = item.itemMeta?.persistentDataContainer
			val destroyableBlocks = container?.get(key, PersistentDataType.STRING)?.split(",") ?: return null
			for (blockData in destroyableBlocks) {
				val parts = blockData.split(":")
				if (parts.size == 4) {
					return parts[3].toDoubleOrNull()
				}
			}
		}
		return null
	}

	private fun calculateDropCount(luck: Double): Int {
		val baseDropCount = luck.toInt()
		val additionalDropChance = luck - baseDropCount

		return if (Random.nextDouble() < additionalDropChance) {
			baseDropCount + 1
		} else {
			baseDropCount
		}
	}
}