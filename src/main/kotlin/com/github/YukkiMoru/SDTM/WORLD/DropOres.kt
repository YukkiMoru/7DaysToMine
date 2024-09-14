package com.github.YukkiMoru.SDTM.WORLD

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

class DropOres(private val plugin: JavaPlugin) : Listener {

	@EventHandler
	fun onBlockBreak(event: BlockBreakEvent) {
		val block = event.block
		val player = event.player
		val itemInHand: ItemStack = player.inventory.itemInMainHand

		if (block.type == Material.COAL_ORE || block.type == Material.IRON_ORE || block.type == Material.DEEPSLATE_IRON_ORE || block.type == Material.RED_STAINED_GLASS) {
			event.isDropItems = false
			val key = NamespacedKey(plugin, "destroyable_blocks")
			val luck = MiningOres.getBlockFortune(itemInHand, key, block.type) ?: 0.0

			val dropCount = calculateDropCount(luck)
			val dropItem = when (block.type) {
				Material.COAL_ORE -> ItemStack(Material.COAL, dropCount)
				Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE -> ItemStack(Material.RAW_IRON, dropCount)
				Material.RED_STAINED_GLASS -> ItemStack(Material.RED_DYE, dropCount)
				else -> return
			}
			block.world.dropItemNaturally(block.location, dropItem)
		}
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