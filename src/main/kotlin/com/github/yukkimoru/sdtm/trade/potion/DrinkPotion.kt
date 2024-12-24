package com.github.yukkimoru.sdtm.trade.potion

import com.github.yukkimoru.sdtm.utility.NumConv
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
			val potionName = container.get(NamespacedKey(plugin, "potion_name"), PersistentDataType.STRING)
			val potionLevel = container.get(NamespacedKey(plugin, "potion_level"), PersistentDataType.INTEGER) ?: 0
			val duration = container.get(NamespacedKey(plugin, "duration"), PersistentDataType.INTEGER) ?: 0

			if (potionName != null) {
				val cooldowns = playerCooldowns[player]
				if (cooldowns != null && cooldowns.containsKey(potionName)) {
					player.sendMessage("§cクールダウン中です：残り${cooldowns[potionName]}秒")
					event.isCancelled = true
					return
				}

				when (potionName to potionLevel) {
					"§c治癒" to 1 -> {
						// Healing Potion Level 1
					}
					"§6力" to 1 -> {
						// Strength Potion Level 1
					}
					"§b俊敏" to 1 -> {
						// Speed Potion Level 1
					}
					"§a巨人" to 1 -> {
						// Giant Potion Level 1
						smoothScale(player, 1.0, 2.0, 20, 10)
					}
					"§e小人" to 1 -> {
						// Midget Potion Level 1
						player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = 0.5
					}
					else -> {
						player.sendMessage("§cUnknown PotionName: $potionName or Level: $potionLevel")
					}
				}
				player.sendMessage("§a${potionName}のポーション${NumConv.intToRoman((potionLevel))}を飲んだ！")
				setTimer(player, potionName, duration)
				startCooldown(player, potionName, duration)
			} else {
				// player.sendMessage("§cPotionName not found.")
			}
		} else {
			// player.sendMessage("§cYou drank a non-potion item.")
		}
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

		when (potionName) {
			"§c治癒" -> {
				// Strength Potion effect ends
			}
			"§6力" -> {
				// Speed Potion effect ends
			}
			"§b俊敏" -> {
				// Giant Potion effect ends
				smoothScale(player, 2.0, 1.0, 20, 10)
			}
			"§a巨人" -> {
				// Midget Potion effect ends
				player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = 1.0
			}
			"§e小人" -> {
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