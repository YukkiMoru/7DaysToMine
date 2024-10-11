package com.github.yukkimoru.sdtm.utility.potions

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import kotlin.math.abs
import kotlin.math.min

class DrinkPotion(private val plugin: Plugin, private val potionList: PotionList) : Listener {

	@EventHandler
	fun onPlayerDrink(event: PlayerItemConsumeEvent) {
		val player = event.player
		val item = event.item

		if (item.type == Material.POTION) {
			val meta = item.itemMeta as PotionMeta
			val container = meta.persistentDataContainer
			val potionID = container.get(NamespacedKey(plugin, "PotionID"), PersistentDataType.INTEGER)
			val duration = container.get(NamespacedKey(plugin, "duration"), PersistentDataType.INTEGER) ?: 0

			if (potionID != null) {
				if (potionList.hasActiveCooldown(player, potionID)) {
					player.sendMessage("§cクールダウン中です")
					event.isCancelled = true
					return
				}

				applyPotionEffect(player, potionID, duration)
				potionList.addPotion(player, potionID, duration)
			} else {
				player.sendMessage("§cPotionID not found.")
			}
		} else {
			player.sendMessage("§cYou drank a non-potion item.")
		}
	}

	private fun applyPotionEffect(player: Player, potionID: Int, duration: Int) {
		when (potionID) {
			1 -> {
				// Healing Potion
				player.health = min(player.health + 4, player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value)
				player.sendMessage("§aYou drank a Healing Potion!")
			}
			2 -> {
				// Strength Potion
				player.sendMessage("§aYou drank a Strength Potion!")
			}
			3 -> {
				// Speed Potion
				player.sendMessage("§aYou drank a Speed Potion!")
			}
			4 -> {
				// Giant Potion
				smoothScale(player, 1.0, 2.0, 20, 10)
				player.sendMessage("§aYou drank a Giant Potion!")
			}
			5 -> {
				// Midget Potion
				player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = 0.5
				player.sendMessage("§aYou drank a Midget Potion!")
			}
			else -> {
				player.sendMessage("§cUnknown PotionID: $potionID")
			}
		}
	}

	private fun smoothScale(player: Player, startScale: Double, endScale: Double, duration: Long, steps: Int) {
		val stepDuration = duration / steps
		val scaleStep = abs(startScale - endScale) / steps
		val increasing = startScale < endScale

		var task: BukkitTask? = null
		task = Bukkit.getScheduler().runTaskTimer(plugin, object : Runnable {
			var currentStep = 0
			override fun run() {
				if (currentStep >= steps) {
					player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = endScale
					task?.cancel()
					return
				}
				val newScale = if (increasing) {
					startScale + (scaleStep * currentStep)
				} else {
					startScale - (scaleStep * currentStep)
				}
				player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = newScale
				currentStep++
			}
		}, 0L, stepDuration)
	}

	fun getCooldowns(player: Player): Map<Int, Int> {
		return potionList.playerPotions[player]?.associate { it.potionID to it.duration } ?: emptyMap()
	}
}