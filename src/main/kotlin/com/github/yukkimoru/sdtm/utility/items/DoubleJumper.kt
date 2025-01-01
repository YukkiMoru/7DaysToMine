package com.github.yukkimoru.sdtm.utility.items

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import org.bukkit.GameMode
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.plugin.java.JavaPlugin

class DoubleJumper(private val plugin: JavaPlugin) : Listener {

	private val cooldownTime = 3000 // 3 seconds
	private var wearArmor: Boolean = false
	private val debugMode = false

	@EventHandler
	fun onPlayerToggleFlight(event: PlayerToggleFlightEvent) {
		if (wearArmor) {
			val player = event.player
			if (player.gameMode == GameMode.CREATIVE) return // クリエ時の無効化

			event.isCancelled = true
			player.allowFlight = false
			player.velocity = player.location.direction.multiply(1.0).setY(1)

			ItemLibrary(plugin).delay(
				cooldownTime / (cooldownTime / 50).toLong()
			) {
				player.allowFlight = true
				player.world.playSound(player.location, "entity.wither.shoot", 0.05f, 0.1f)
				if (debugMode) player.sendMessage("§a2ダブルジャンプが可能!")
			}
		}
	}

	@EventHandler
	fun onPlayerArmorChange(event: PlayerArmorChangeEvent) {
		val player = event.player
		ItemLibrary(plugin).delay(
			1L
		) {
			wearArmor = ItemLibrary(plugin).isWearingEquip(player, "boots", 301)
			if (wearArmor) {
				if (debugMode) player.sendMessage("§a2段ジャンプ装備を装備しました")
				player.allowFlight = true
				// デフォルトのGENERIC_SAFE_FALL_DISTANCEは3.0
				player.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE)?.baseValue = 8.0
			} else {
				if (debugMode) player.sendMessage("§c2段ジャンプ装備を外しました")
				player.allowFlight = false
				player.getAttribute(Attribute.GENERIC_SAFE_FALL_DISTANCE)?.baseValue = 3.0
			}
		}
	}
}