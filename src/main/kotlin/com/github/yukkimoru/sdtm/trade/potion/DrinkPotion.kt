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
			val potionNameLevel: String? =
				container.get(NamespacedKey(plugin, "potion_name_level"), PersistentDataType.STRING)

			if (potionNameLevel == null) {
				player.sendMessage("§cポーションの名前とレベルが見つかりませんでした。")
				return
			}

			val (potionName, potionLevel) = potionNameLevel.split("/").let { it[0] to it[1].toInt() }
			val potionFactory = PotionFactory(plugin)
			potionName.let {
				potionFactory.getPotionInfo(potionNameLevel)?.let { potionData ->
					val duration = potionData.duration
					val cooldowns = playerCooldowns[player]
					if (cooldowns != null && cooldowns.containsKey(potionName)) {
						player.sendMessage("§cクールダウン中です：残り${cooldowns[potionName]}秒")
						event.isCancelled = true
						return
					}

					when (potionNameLevel) {
						"healing/1" -> {
						}

						"strength/1" -> {
						}

						"speed/1" -> {
						}

						"giant/1" -> {
							smoothScale(player, 1.0, 2.0, 20, 10)
						}

						"giant/2" -> {
							smoothScale(player, 1.0, 3.0, 20, 10)
						}

						"giant/3" -> {
							smoothScale(player, 1.0, 4.0, 20, 10)
						}

						"giant/4" -> {
							smoothScale(player, 1.0, 5.0, 20, 10)
						}

						"midget/1" -> {
							smoothScale(player, 1.0, 0.5, 20, 10)
						}

						"midget/2" -> {
							smoothScale(player, 1.0, 0.25, 20, 10)
						}

						"midget/3" -> {
							smoothScale(player, 1.0, 0.125, 20, 10)
						}

						"midget/4" -> {
							smoothScale(player, 1.0, 0.0625, 20, 10)
						}
					}
					player.sendMessage("§a${potionNameLevel}を飲んだ！")
					startCooldown(player, potionNameLevel, duration)
				} ?: run {
					player.sendMessage("§cポーション情報が見つかりませんでした。")
				}
			}
		} else {
			// player.sendMessage("§cYou drank a non-potion item.")
		}
	}

	private fun durationEnd(
		player: Player,
		potionNameLevel: String,
	) {
		potionEffectTask?.cancel()
		potionEffectTask = null

		playerCooldowns[player]?.remove(potionNameLevel.toString())

		when (potionNameLevel) {
			"giant/1" -> {
				smoothScale(player, 2.0, 1.0, 20, 10)
			}

			"giant/2" -> {
				smoothScale(player, 3.0, 1.0, 20, 10)
			}

			"giant/3" -> {
				smoothScale(player, 4.0, 1.0, 20, 10)
			}

			"giant/4" -> {
				smoothScale(player, 5.0, 1.0, 20, 10)
			}

			"midget/1" -> {
				smoothScale(player, 0.5, 1.0, 20, 10)
			}

			"midget/2" -> {
				smoothScale(player, 0.25, 1.0, 20, 10)
			}

			"midget/3" -> {
				smoothScale(player, 0.125, 1.0, 20, 10)
			}

			"midget/4" -> {
				smoothScale(player, 0.0625, 1.0, 20, 10)
			}
		}
		player.sendMessage("§c${potionNameLevel}の効果が切れた！")
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
				player.getAttribute(Attribute.GENERIC_SCALE)?.let {
					it.baseValue = newScale
				}
				currentStep++
			}
		}, 0L, stepDuration)
	}

	private fun startCooldown(player: Player, potionNameLevel: String, duration: Int) {
		val cooldowns = playerCooldowns.getOrPut(player) { mutableMapOf() }
		cooldowns[potionNameLevel] = duration

		potionEffectTask = Bukkit.getScheduler().runTaskTimer(plugin, object : Runnable {
			var remainingTime = duration
			override fun run() {
				if (remainingTime < 0) {
					potionEffectTask?.cancel()
					durationEnd(player, potionNameLevel)
					return
				}
				cooldowns[potionNameLevel] = remainingTime
				remainingTime--
			}
		}, 0L, 20L)
	}

	fun getCooldowns(player: Player): Map<String, Int> {
		return playerCooldowns[player] ?: emptyMap()
	}
}