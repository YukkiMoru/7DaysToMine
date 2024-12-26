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

	@Suppress("SpellCheckingInspection")
	@EventHandler
	fun onPlayerDrink(event: PlayerItemConsumeEvent) {
		val player = event.player
		val item = event.item

		if (item.type == Material.POTION) {
			val meta = item.itemMeta as PotionMeta
			val container = meta.persistentDataContainer
			val potionName: String? =
				container.get(NamespacedKey(plugin, "potion_name"), PersistentDataType.STRING)
			val potionLevel: Int? =
				container.get(NamespacedKey(plugin, "potion_level"), PersistentDataType.INTEGER)
			if (potionLevel == null) {
				player.sendMessage("§cInvalid potion level.")
				return
			}
			val potionFactory = PotionFactory(plugin)
			potionName?.let {
				potionFactory.getPotionInfo(potionName, potionLevel)?.let { potionData ->
					val potionName = potionData.potionName
					val duration = potionData.duration
					val cooldowns = playerCooldowns[player]
					if (cooldowns != null && cooldowns.containsKey(potionName)) {
						player.sendMessage("§cクールダウン中です：残り${cooldowns[potionName]}秒")
						event.isCancelled = true
						return
					}

					when (potionName) {
						"Healing" -> {
							when (potionLevel) {
								1 -> {
									// Healing Potion Level 1
								}
								// 他のレベルの処理
							}
						}

						"Strength" -> {
							when (potionLevel) {
								1 -> {
									// Strength Potion Level 1
								}
								// 他のレベルの処理
							}
						}

						"Speed" -> {
							when (potionLevel) {
								1 -> {
									// Speed Potion Level 1
								}
								// 他のレベルの処理
							}
						}

						"Giant" -> {
							when (potionLevel) {
								1 -> {
									// Giant Potion Level 1
									smoothScale(player, 1.0, 2.0, 20, 10)
								}
								// 他のレベルの処理
							}
						}

						"Midget" -> {
							when (potionLevel) {
								1 -> {
									// Midget Potion Level 1
									player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = 0.5
								}
								// 他のレベルの処理
							}
						}
					}
					player.sendMessage("§a${potionName}のポーション,potionLevel:${potionLevel}を飲んだ！")
					setTimer(player, potionName, potionLevel, duration)
					startCooldown(player, potionName, duration)
				}
			}
		} else {
			// player.sendMessage("§cYou drank a non-potion item.")
		}
	}

	private fun setTimer(
		player: Player,
		potionName: String,
		potionLevel: Int,
		duration: Int
	) {
		potionEffectTask = Bukkit.getScheduler().runTaskLater(plugin, Runnable {
			removeTimer(player, potionName, potionLevel)
		}, duration * 20L)
	}

	private fun removeTimer(
		player: Player,
		potionName: String,
		potionLevel: Int
	) {
		potionEffectTask?.cancel()
		potionEffectTask = null

		playerCooldowns[player]?.remove(potionName.toString())

		when (potionName) {
			"Healing" -> {
				when (potionLevel) {
					1 -> {
						// Healing Potion Level 1
					}
					// 他のレベルの処理
				}
			}

			"Strength" -> {
				when (potionLevel) {
					1 -> {
						// Strength Potion Level 1
					}
					// 他のレベルの処理
				}
			}

			"Speed" -> {
				when (potionLevel) {
					1 -> {
						// Speed Potion Level 1
					}
					// 他のレベルの処理
				}
			}

			"Giant" -> {
				when (potionLevel) {
					1 -> {
						// Giant Potion Level 1
						smoothScale(player, 2.0, 1.0, 20, 10)
					}
					// 他のレベルの処理
				}
			}

			"Midget" -> {
				when (potionLevel) {
					1 -> {
						// Midget Potion Level 1
						player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = 0.5
					}
					// 他のレベルの処理
				}
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
		var task: BukkitTask? = null
		task = Bukkit.getScheduler().runTaskTimer(plugin, object : Runnable {
			override fun run() {
				val timeLeft = cooldowns[potionName] ?: return
				if (timeLeft > 0) {
					cooldowns[potionName] = timeLeft - 1
				} else {
					cooldowns.remove(potionName)
					task?.cancel()
				}
			}
		}, 0L, 20L)
	}

	fun getCooldowns(player: Player): Map<String, Int> {
		return playerCooldowns[player] ?: emptyMap()
	}
}