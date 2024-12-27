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

	private val playerCooldowns = mutableMapOf<Player, MutableMap<String, Int>>()
	private val playerTasks = mutableMapOf<Player, MutableMap<String, BukkitTask>>()

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
						"healing" -> {
							when (potionLevel) {
								1 -> {
									// Healing Potion Level 1
								}
								// 他のレベルの処理
							}
						}

						"strength" -> {
							when (potionLevel) {
								1 -> {
									// Strength Potion Level 1
								}
								// 他のレベルの処理
							}
						}

						"speed" -> {
							when (potionLevel) {
								1 -> {
									// Speed Potion Level 1
								}
								// 他のレベルの処理
							}
						}

						"giant" -> {
							when (potionLevel) {
								1 -> {
									// Giant Potion Level 1
									smoothScale(player, 1.0, 2.0, 20, 10)
								}
								// 他のレベルの処理
							}
						}

						"midget" -> {
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
					startCooldown(player, potionName, potionLevel, duration)
				}
			}
		} else {
			// player.sendMessage("§cYou drank a non-potion item.")
		}
	}

	private fun durationEnd(
		player: Player,
		potionName: String,
		potionLevel: Int
	) {
		playerTasks[player]?.get(potionName)?.cancel()
		playerTasks[player]?.remove(potionName) // タスクをマップから削除

		playerCooldowns[player]?.remove(potionName)

		when (potionName) {
			"healing" -> {
				when (potionLevel) {
					1 -> {
						// Healing Potion Level 1
					}
					// 他のレベルの処理
				}
			}

			"strength" -> {
				when (potionLevel) {
					1 -> {
						// Strength Potion Level 1
					}
					// 他のレベルの処理
				}
			}

			"speed" -> {
				when (potionLevel) {
					1 -> {
						// Speed Potion Level 1
					}
					// 他のレベルの処理
				}
			}

			"giant" -> {
				when (potionLevel) {
					1 -> {
						// Giant Potion Level 1
						smoothScale(player, 2.0, 1.0, 20, 10)
					}
					// 他のレベルの処理
				}
			}

			"midget" -> {
				when (potionLevel) {
					1 -> {
						// Midget Potion Level 1
						player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = 1.0
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

		val task = Bukkit.getScheduler().runTaskTimer(plugin, object : Runnable {
			var currentStep = 0
			override fun run() {
				if (currentStep >= steps) {
					player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = endScale
					playerTasks[player]?.remove("smoothScale")?.cancel()
					return
				}
				val newScale = if (increasing) {
					startScale + (scaleStep * currentStep)
				} else {
					startScale - (scaleStep * currentStep)
				}
				player.getAttribute(Attribute.GENERIC_SCALE)?.let {
					it.baseValue = newScale
				}
				currentStep++
			}
		}, 0L, stepDuration)

		val tasks = playerTasks.getOrPut(player) { mutableMapOf() }
		tasks["smoothScale"] = task
	}

	private fun startCooldown(player: Player, potionName: String, potionLevel: Int, duration: Int) {
		val cooldowns = playerCooldowns.getOrPut(player) { mutableMapOf() }
		cooldowns[potionName] = duration

		val task = Bukkit.getScheduler().runTaskTimer(plugin, object : Runnable {
			var remainingTime = duration
			override fun run() {
				if (remainingTime < 0) {
					playerTasks[player]?.get(potionName)?.cancel()
					playerTasks[player]?.remove(potionName) // タスクをマップから削除
					durationEnd(player, potionName, potionLevel)
					return
				}
				cooldowns[potionName] = remainingTime
				remainingTime--
			}
		}, 0L, 20L)

		val tasks = playerTasks.getOrPut(player) { mutableMapOf() }
		tasks[potionName] = task
	}

	fun getCooldowns(player: Player): Map<String, Int> {
		return playerCooldowns[player] ?: emptyMap()
	}
}