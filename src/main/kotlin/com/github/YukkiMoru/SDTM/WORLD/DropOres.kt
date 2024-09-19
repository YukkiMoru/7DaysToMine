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

	private val itemCreater = ItemCreater(plugin)

	@EventHandler
	fun onBlockBreak(event: BlockBreakEvent) {
		val block = event.block
		val player = event.player
		val itemInHand: ItemStack = player.inventory.itemInMainHand

		if (block.type == Material.COAL_ORE || block.type == Material.IRON_ORE || block.type == Material.DEEPSLATE_IRON_ORE || block.type == Material.RED_STAINED_GLASS || block.type == Material.RED_STAINED_GLASS_PANE) {
			event.isDropItems = false
			val key = NamespacedKey(plugin, "destroyable_blocks")
			val luck = MiningOres.getBlockFortune(itemInHand, key, block.type) ?: 0.0

			val dropCount = calculateDropCount(luck)
			if (dropCount > 0) {
				val dropItem = when (block.type) {
					Material.COAL_ORE -> itemCreater.CreateItemStack(
						Material.CHARCOAL,
						dropCount,
						"§7§l褐炭",
						listOf("§7燃料として使える", "§c§l☀780秒"),
						"common"
					)

					Material.IRON_ORE -> itemCreater.CreateItemStack(
						Material.RAW_IRON,
						dropCount,
						"§f§l低品質な鉄鉱石",
						listOf("§7粗悪な鉄インゴットをつくれる"),
						"common"
					)

					Material.DEEPSLATE_IRON_ORE -> itemCreater.CreateItemStack(
						Material.RAW_IRON,
						dropCount,
						"§f§l普通の鉄鉱石",
						listOf("§7そこそこの品質の鉄インゴットをつくれる"),
						"uncommon"
					)

					Material.RED_STAINED_GLASS, Material.RED_STAINED_GLASS_PANE -> itemCreater.CreateItemStack(
						Material.RED_DYE,
						dropCount,
						"§c§lルビー",
						listOf("§c古代から愛され続けている宝石"),
						"common"
					)

					else -> return
				}
				block.world.dropItemNaturally(block.location, dropItem)
			}
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