package com.github.yukkimoru.sdtm.utility

import org.bukkit.GameMode
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class DoubleJump(private val plugin: JavaPlugin) : Listener {
	init {
		plugin.server.onlinePlayers.forEach { player ->
			player.allowFlight = true
		}
	}

	private val doubleJumpPlayers = mutableSetOf<String>()
	private val flyingPlayers = mutableSetOf<UUID>()
	private val lastJumpTime = mutableMapOf<UUID, Long>()
	private val cooldownTime = 3000 // 3 seconds in milliseconds

	@EventHandler
	fun onPlayerToggleFlight(event: PlayerToggleFlightEvent) {
		val player = event.player

		if (player.gameMode == GameMode.CREATIVE) return

		event.isCancelled = true

		val currentTime = System.currentTimeMillis()
		val playerUUID = player.uniqueId

		if (doubleJumpPlayers.contains(player.name)) {
			player.allowFlight = false
			doubleJumpPlayers.remove(player.name)
		} else {
			val lastJump = lastJumpTime[playerUUID] ?: 0
			if (currentTime - lastJump >= cooldownTime) {
				player.velocity = player.location.direction.multiply(1.5).setY(1)
				doubleJumpPlayers.add(player.name)
				lastJumpTime[playerUUID] = currentTime
			}
		}
		object : BukkitRunnable() {
			override fun run() {
				player.allowFlight = true
				player.world.playSound(player.location, "entity.wither.shoot", 0.05f, 0.1f)
			}
		}.runTaskLater(plugin, 40L)
	}

	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		val player = event.player
		if (flyingPlayers.contains(player.uniqueId)) {
			player.allowFlight = true
		}
	}
}