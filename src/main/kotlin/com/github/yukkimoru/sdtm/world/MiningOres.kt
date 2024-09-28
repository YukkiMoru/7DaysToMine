package com.github.yukkimoru.sdtm.world

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerSwapHandItemsEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class MiningOres(private val plugin: JavaPlugin) : Listener {

	private val playerTargetBlocks = mutableMapOf<UUID, Material?>()

	init {
		object : BukkitRunnable() {
			override fun run() {
				for (player in Bukkit.getOnlinePlayers()) {
					val targetBlock = player.getTargetBlockExact(5)
					val previousBlock = playerTargetBlocks[player.uniqueId]
					if (targetBlock != null && targetBlock.type != Material.AIR) {
						if (player.getAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED)?.value == 0.0) {
							player.sendActionBar(
								Component.text(
									previousBlock?.name ?: "No previous block",
									NamedTextColor.RED
								)
							)
						} else {
							player.sendActionBar(
								Component.text(
									previousBlock?.name ?: "No previous block",
									NamedTextColor.GREEN
								)
							)
//							// message break speed and fortune
//							player.sendMessage("Break speed: ${player.getAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED)?.value}")
//							player.sendMessage("Fortune: ${player.getAttribute(Attribute.GENERIC_LUCK)?.value}")
						}
					} else {
						player.sendActionBar(Component.text(previousBlock?.name ?: "No previous block"))
					}
					if (targetBlock?.type != previousBlock) {
						playerTargetBlocks[player.uniqueId] = targetBlock?.type
						checkAndSet(player, targetBlock?.type)
					}
				}
			}
		}.runTaskTimer(plugin, 0L, 1L) // Run every 1 ticks (0.05 seconds)
	}

	@EventHandler
	fun onItemHeld(event: PlayerItemHeldEvent) {
		val player = event.player
		val targetBlock = player.getTargetBlockExact(5)
		// wait 1 tick
		Bukkit.getScheduler().runTaskLater(plugin, Runnable {
			checkAndSet(player, targetBlock?.type)
		}, 1L)
	}

	@EventHandler
	fun onSwapHandItems(event: PlayerSwapHandItemsEvent) {
		val player = event.player
		val targetBlock = player.getTargetBlockExact(5)
		Bukkit.getScheduler().runTaskLater(plugin, Runnable {
			checkAndSet(player, targetBlock?.type)
		}, 1L)
	}

	private fun checkAndSet(player: Player, targetBlockType: Material?) {
		val itemInHand: ItemStack = player.inventory.itemInMainHand
		val itemInOffHand: ItemStack = player.inventory.itemInOffHand
		val key = NamespacedKey(plugin, "destroyable_blocks")

		val speed = getBlockBreakSpeed(itemInHand, key, targetBlockType)
			?: getBlockBreakSpeed(itemInOffHand, key, targetBlockType)
			?: 0.0

		val fortune = getBlockFortune(itemInHand, key, targetBlockType)
			?: getBlockFortune(itemInOffHand, key, targetBlockType)
			?: 0.0

		setBlockBreakSpeed(player, speed)
		setBlockFortune(player, fortune)
	}

	// minecraft:coal_ore:0.7:3.0,minecraft:iron_ore:0.8:1.0,minecraft:deepslate_iron_ore:0.3:1.0
	private fun getBlockBreakSpeed(item: ItemStack, key: NamespacedKey, targetBlockType: Material?): Double? {
		if (item.type == Material.NETHERITE_PICKAXE) {
			val container = item.itemMeta?.persistentDataContainer
			val destroyableBlocks = container?.get(key, PersistentDataType.STRING)?.split(",") ?: return null
			for (blockData in destroyableBlocks) {
				val parts = blockData.split(":")
				if (parts.size == 4 && parts[0] == targetBlockType?.key?.namespace && parts[1] == targetBlockType.key.key) {
					return parts[2].toDoubleOrNull()
				}
			}
		}
		return null
	}

	private fun getBlockFortune(item: ItemStack, key: NamespacedKey, targetBlockType: Material?): Double? {
		if (item.type == Material.NETHERITE_PICKAXE) {
			val container = item.itemMeta?.persistentDataContainer
			val destroyableBlocks = container?.get(key, PersistentDataType.STRING)?.split(",") ?: return null
			for (blockData in destroyableBlocks) {
				val parts = blockData.split(":")
				if (parts.size == 4 && parts[0] == targetBlockType?.key?.namespace && parts[1] == targetBlockType.key.key) {
					return parts[3].toDoubleOrNull()
				}
			}
		}
		return null
	}

	private fun setBlockBreakSpeed(player: Player, speed: Double) {
		val attribute = player.getAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED)
		attribute?.modifiers?.forEach { attribute.removeModifier(it) }
		val modifier = AttributeModifier(NamespacedKey(plugin, "block_break_speed"), speed, Operation.ADD_NUMBER)
		attribute?.addModifier(modifier)
	}

	private fun setBlockFortune(player: Player, fortune: Double) {
		val attribute = player.getAttribute(Attribute.GENERIC_LUCK)
		attribute?.modifiers?.forEach { attribute.removeModifier(it) }
		val modifier = AttributeModifier(NamespacedKey(plugin, "block_fortune"), fortune, Operation.ADD_NUMBER)
		attribute?.addModifier(modifier)
	}

	companion object {
		fun getBlockFortune(item: ItemStack, key: NamespacedKey, targetBlockType: Material?): Double? {
			if (item.type == Material.NETHERITE_PICKAXE) {
				val container = item.itemMeta?.persistentDataContainer
				val destroyableBlocks = container?.get(key, PersistentDataType.STRING)?.split(",") ?: return null
				for (blockData in destroyableBlocks) {
					val parts = blockData.split(":")
					if (parts.size == 4 && parts[0] == targetBlockType?.key?.namespace && parts[1] == targetBlockType.key.key) {
						return parts[3].toDoubleOrNull()
					}
				}
			}
			return null
		}
	}
}