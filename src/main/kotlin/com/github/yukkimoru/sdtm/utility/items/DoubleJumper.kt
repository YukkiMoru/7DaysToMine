package com.github.yukkimoru.sdtm.utility.items

import com.destroystokyo.paper.event.player.PlayerArmorChangeEvent
import com.github.yukkimoru.sdtm.utility.ItemFactory
import org.bukkit.GameMode
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerToggleFlightEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class DoubleJumper(private val plugin: JavaPlugin) : Listener {

	private val doubleJumpPlayers = mutableSetOf<String>()
	private val lastJumpTime = mutableMapOf<UUID, Long>()
	private val cooldownTime = 3000 // 3 seconds
	private var wearArmor: Boolean = false

	// なにも装備していない(null), または301のカスタムモデルデータを持つアイテムを装備しているか
	private fun isWearingDoubleJumperBoots(player: Player): Boolean {
		val boots: ItemStack? = player.inventory.boots
		return boots?.let { ItemFactory(plugin).isItemWithCustomModelData(it, 301) } ?: false
	}

	@EventHandler
	fun onPlayerToggleFlight(event: PlayerToggleFlightEvent) {
		if (wearArmor) {
			val player = event.player
			if (player.gameMode == GameMode.CREATIVE) return
			event.isCancelled = true

			val currentTime = System.currentTimeMillis()
			val playerUUID = player.uniqueId

			if (doubleJumpPlayers.contains(player.name)) {
				//↓使うとクールダウン中に２段ジャンプすると落下ダメージを食らう
//			player.allowFlight = false
				player.sendMessage("§aDouble Jump is on cooldown!")
			} else {
				player.sendMessage("§cDouble Jump")
				val lastJump = lastJumpTime[playerUUID] ?: 0
				if (currentTime - lastJump >= cooldownTime) {
					player.velocity = player.location.direction.multiply(1.0).setY(1)
					doubleJumpPlayers.add(player.name)
					lastJumpTime[playerUUID] = currentTime

					object : BukkitRunnable() {
						override fun run() {
							if (wearArmor) {
								player.allowFlight = true
								player.world.playSound(player.location, "entity.wither.shoot", 0.05f, 0.1f)
								player.sendMessage("§aDouble Jump is ready!")
							}
							doubleJumpPlayers.remove(player.name)
						}
					}.runTaskLater(plugin, (cooldownTime / 50).toLong()) // Convert milliseconds to ticks
				}
			}
		}
	}

//	@EventHandler
//	fun onPlayerJoin(event: PlayerJoinEvent) {
//		val player = event.player
//		if (flyingPlayers.contains(player.uniqueId)) {
//			player.allowFlight = true
//		}
//	}

	@EventHandler
	fun onPlayerArmorChange(event: PlayerArmorChangeEvent) {
		val player = event.player

		object : BukkitRunnable() {
			override fun run() {
				wearArmor = isWearingDoubleJumperBoots(player)
				if (wearArmor) {
					player.sendMessage("§aYou are wearing Double Jumper Boots by equip!")
					player.allowFlight = true
				} else {
					player.sendMessage("§cYou are not wearing Double Jumper Boots by equip!")
					player.allowFlight = false
				}
			}
		}.runTaskLater(plugin, 1L) // Delay by 1 tick
	}
}