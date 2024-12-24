package com.github.yukkimoru.sdtm.trade.potion

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
import kotlin.math.abs

@Suppress("SameParameterValue")
class DrinkPotion(private val plugin: Plugin) : Listener {

	private var potionEffectTask: BukkitTask? = null
	private val playerCooldowns = mutableMapOf<Player, MutableMap<String, Int>>()

	@EventHandler
	fun onPlayerDrink(event: PlayerItemConsumeEvent) {
		val player = event.player
		val item = event.item

		if (item.type == Material.POTION) {
			val meta = item.itemMeta as PotionMeta
			val container = meta.persistentDataContainer
			val customModelID = container.get(NamespacedKey(plugin, "custom_model_id"), PersistentDataType.INTEGER)

			val potionFactory = PotionFactory(plugin)
			customModelID?.let {
				potionFactory.getPotionInfo(it)?.let { potionData ->
					val potionName = potionData.potionName
					val duration = potionData.duration
					drinkPotion(player, potionName, it, duration, event)

					val cooldowns = playerCooldowns[player]
					if (cooldowns != null && cooldowns.containsKey(potionName)) {
						player.sendMessage("§cクールダウン中です：残り${cooldowns[potionName]}秒")
						event.isCancelled = true
						return
					}

					when (it) {
						1 -> {
							// Healing Potion Level 1
						}

						2 -> {
							// Strength Potion Level 1
						}

						3 -> {
							// Speed Potion Level 1
						}

						4 -> {
							// Giant Potion Level 1
							smoothScale(player, 1.0, 2.0, 20, 10)
						}

						5 -> {
							// Midget Potion Level 1
							player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = 0.5
						}

						else -> {
							player.sendMessage("§a${potionName}のポーション,customModelID:${customModelID}を飲んだ！")
						}
					}
					player.sendMessage("§a${potionName}のポーション,customModelID:${customModelID}を飲んだ！")
					setTimer(player, potionName, duration)
					startCooldown(player, potionName, duration)
				}
			}
		} else {
			// player.sendMessage("§cYou drank a non-potion item.")
		}
	}

	private fun drinkPotion(
		player: Player,
		potionName: String,
		customModelID: Int,
		duration: Int,
		event: PlayerItemConsumeEvent
	) {
		// Example implementation
		player.sendMessage("You drank a potion: $potionName for $duration seconds.")
	}

	private fun setTimer(player: Player, potionName: String, duration: Int) {
		potionEffectTask = Bukkit.getScheduler().runTaskLater(plugin, Runnable {
			removeTimer(player, potionName)
		}, duration * 20L)
	}

	private fun removeTimer(player: Player, potionName: String) {
		potionEffectTask?.cancel()
		potionEffectTask = null

		playerCooldowns[player]?.remove(potionName)

		when (potionName.toInt()) {
			1 -> {
				// Strength Potion effect ends
			}

			2 -> {
				// Speed Potion effect ends
			}

			3 -> {
				// Giant Potion effect ends
				smoothScale(player, 2.0, 1.0, 20, 10)
			}

			4 -> {
				// Midget Potion effect ends
				player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = 1.0
			}

			5 -> {
				// Midget Potion effect ends
				player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = 1.0
			}

			else -> {
				player.sendMessage("§cThe effect of potion $potionName has worn off.")
			}
		}
		player.sendMessage("§c${potionName}の効果が切れた！")
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

	private fun startCooldown(player: Player, potionName: String, duration: Int) {
		val cooldowns = playerCooldowns.getOrPut(player) { mutableMapOf() }
		cooldowns[potionName] = duration
		Bukkit.getScheduler().runTaskTimer(plugin, object : Runnable {
			override fun run() {
				val timeLeft = cooldowns[potionName] ?: return
				if (timeLeft > 0) {
					cooldowns[potionName] = timeLeft - 1
				} else {
					cooldowns.remove(potionName)
					Bukkit.getScheduler().cancelTask(this.hashCode())
				}
			}
		}, 0L, 20L)
	}

	fun getCooldowns(player: Player): Map<String, Int> {
		return playerCooldowns[player] ?: emptyMap()
	}
}