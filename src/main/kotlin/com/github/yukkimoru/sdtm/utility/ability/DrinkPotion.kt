package com.github.yukkimoru.sdtm.utility.ability

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
import org.bukkit.scheduler.BukkitTask
import kotlin.math.min

class DrinkPotion(private val plugin: Plugin) : Listener {

	private var potionEffectTask: BukkitTask? = null

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
				when (potionID) {
					1 -> {
						// Healing Potion
						player.health =
							min(player.health + 4, player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value)
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
				setTimer(player, potionID, duration)
			} else {
				player.sendMessage("§cPotionID not found.")
			}
		} else {
			player.sendMessage("§cYou drank a non-potion item.")
		}
	}


	/**
	 * Sets a timer for the potion effect.
	 * @param player The player who drank the potion.
	 * @param potionID The ID of the potion effect.
	 * @param duration The duration of the potion effect in seconds.
	 */
	private fun setTimer(player: Player, potionID: Int, duration: Int) {
		potionEffectTask = Bukkit.getScheduler().runTaskLater(plugin, Runnable {
			removeTimer(player, potionID)
		}, duration * 20L)
	}

	/**
	 * Removes the timer for the potion effect.
	 * @param player The player whose potion effect timer is to be removed.
	 * @param potionID The ID of the potion effect.
	 */
	private fun removeTimer(player: Player, potionID: Int) {
		potionEffectTask?.cancel()
		potionEffectTask = null

		when (potionID) {
			2 -> {
				// Strength Potion effect ends
				player.sendMessage("§cThe effect of the Strength Potion has worn off.")
			}

			3 -> {
				// Speed Potion effect ends
				player.sendMessage("§cThe effect of the Speed Potion has worn off.")
			}

			4 -> {
				// Giant Potion effect ends
				smoothScale(player, 2.0, 1.0, 20, 10)

				player.sendMessage("§cThe effect of the Giant Potion has worn off.")
			}

			5 -> {
				// Midget Potion effect ends
				player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = 1.0
				player.sendMessage("§cThe effect of the Midget Potion has worn off.")
			}

			else -> {
				player.sendMessage("§cThe effect of potion $potionID has worn off.")
			}
		}
	}

	private fun smoothScale(player: Player, startScale: Double, endScale: Double, duration: Long, steps: Int) {
		val stepDuration = duration / steps
		val scaleStep = (startScale - endScale) / steps

		Bukkit.getScheduler().runTaskTimer(plugin, object : Runnable {
			var currentStep = 0
			override fun run() {
				if (currentStep >= steps) {
					player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = endScale
					Bukkit.getScheduler().cancelTask(this.hashCode())
					return
				}
				val newScale = startScale - (scaleStep * currentStep)
				player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = newScale
				currentStep++
			}
		}, 0L, stepDuration)
	}
}