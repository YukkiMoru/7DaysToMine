package com.github.yukkimoru.sdtm.utility.ability

import com.github.yukkimoru.sdtm.world.FactoryItem
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class DoubleJump(private val plugin: JavaPlugin) : Listener {

	private val doubleJumpPlayers = mutableSetOf<String>()
	private val flyingPlayers = mutableSetOf<UUID>()
	private val lastJumpTime = mutableMapOf<UUID, Long>()
	private val cooldownTime = 3000 // 3 seconds

	// なにも装備していない(null), または301のカスタムモデルデータを持つアイテムを装備しているか
	private fun isWearingDoubleJumperBoots(player: Player): Boolean {
		val boots: ItemStack? = player.inventory.boots
		return boots?.let { FactoryItem(plugin).isItemWithCustomModelData(it, 301) } ?: false
	}

	@EventHandler
	fun onPlayerToggleFlight(event: PlayerToggleFlightEvent) {
		val player = event.player

		if (!isWearingDoubleJumperBoots(player)) {
			player.allowFlight = false
			player.sendMessage("§cYou can't double jump!")
			return
		}
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

	@EventHandler
	fun onPlayerItemHeld(event: PlayerItemHeldEvent) {
		val player = event.player
		val boots = player.inventory.boots

		if (boots == null || !FactoryItem(plugin).isItemWithCustomModelData(boots, 301)) {
			player.allowFlight = false
			doubleJumpPlayers.remove(player.name)
		} else {
			player.allowFlight = true
			player.sendMessage("§aYou can now double jump!")
		}
	}
}