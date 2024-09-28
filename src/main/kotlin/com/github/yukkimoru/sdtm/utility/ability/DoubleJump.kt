package com.github.yukkimoru.sdtm.utility.ability

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

		// Check if player is wearing Double Jumper boots
		val boots = player.inventory.boots
		if (boots == null || !boots.hasItemMeta() || boots.itemMeta?.displayName != "§r§2§lDouble Jumper") {
			player.allowFlight = false
			return
		}

		// message to player
		player.sendMessage("§cDouble Jump")

		if (player.gameMode == GameMode.CREATIVE) return

		event.isCancelled = true

		val currentTime = System.currentTimeMillis()
		val playerUUID = player.uniqueId

		if (doubleJumpPlayers.contains(player.name)) {
			player.allowFlight = false
			player.sendMessage("§aDouble Jump is on cooldown!")
		} else {
			val lastJump = lastJumpTime[playerUUID] ?: 0
			if (currentTime - lastJump >= cooldownTime) {
				player.velocity = player.location.direction.multiply(1.0).setY(1)
				doubleJumpPlayers.add(player.name)
				lastJumpTime[playerUUID] = currentTime

				object : BukkitRunnable() {
					override fun run() {
						player.allowFlight = true
						player.world.playSound(player.location, "entity.wither.shoot", 0.05f, 0.1f)
						player.sendMessage("§aDouble Jump is ready!")
						doubleJumpPlayers.remove(player.name)
					}
				}.runTaskLater(plugin, (cooldownTime / 50).toLong()) // Convert milliseconds to ticks
			}
		}
	}

	@EventHandler
	fun onPlayerJoin(event: PlayerJoinEvent) {
		val player = event.player
		if (flyingPlayers.contains(player.uniqueId)) {
			player.allowFlight = true
		}
	}
}