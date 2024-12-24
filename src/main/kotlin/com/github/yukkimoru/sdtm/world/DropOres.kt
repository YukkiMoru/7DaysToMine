package com.github.yukkimoru.sdtm.world

import com.github.yukkimoru.sdtm.utility.ItemFactory
import com.github.yukkimoru.sdtm.trade.pickaxe.ToolFactory
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

class DropOres(private val plugin: JavaPlugin, private val toolFactory: ToolFactory) : Listener {

	private val itemFactory = ItemFactory(plugin)

	@EventHandler
	fun onBlockBreak(event: BlockBreakEvent) {
		val block = event.block
		val player = event.player
		val itemInHand: ItemStack = player.inventory.itemInMainHand

		// Debugging: Log the block type and all breakable materials
//		Bukkit.getLogger().info("Block type: ${block.type}")
//		Bukkit.getLogger().info("All breakable materials: ${factoryTool.allBreakableMaterials}")

		if (block.type in toolFactory.allBreakableMaterials) {
			// message
//			Bukkit.getLogger().info("yes you dug")
			event.isDropItems = false
			val key = NamespacedKey(plugin, "destroyable_blocks")
			val luck = MiningOres.getBlockFortune(itemInHand, key, block.type) ?: 0.0

			val dropCount = calculateDropCount(luck)
			if (dropCount > 0) {
				val dropItem = when (block.type) {
					Material.COAL_ORE, Material.DEEPSLATE_COAL_ORE, Material.COAL_BLOCK -> itemFactory.createItemStack(
						Material.COAL,
						dropCount,
						"§7§l石炭",
						listOf("§7何かを燃やせる"),
						"common"
					)

					Material.IRON_ORE, Material.DEEPSLATE_IRON_ORE, Material.RAW_IRON_BLOCK -> itemFactory.createItemStack(
						Material.RAW_IRON,
						dropCount,
						"§f§l鉄鉱石",
						listOf("§7鉄インゴットをつくれる"),
						"common"
					)

					Material.RED_STAINED_GLASS, Material.RED_STAINED_GLASS_PANE -> itemFactory.createItemStack(
						Material.RED_DYE,
						dropCount,
						"§c§lルビー",
						listOf("§7古代から愛され続けている宝石"),
						"common",
						1
					)

					Material.ORANGE_STAINED_GLASS -> itemFactory.createItemStack(
						Material.ORANGE_DYE,
						dropCount,
						"§6§lアンバー",
						listOf("§7樹液が化石化したもの"),
						"common",
						2
					)

					Material.BLUE_STAINED_GLASS, Material.BLUE_STAINED_GLASS_PANE -> itemFactory.createItemStack(
						Material.BLUE_DYE,
						dropCount,
						"§9§lサファイア",
						listOf("§7神秘的な青色の宝石"),
						"common",
						3
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