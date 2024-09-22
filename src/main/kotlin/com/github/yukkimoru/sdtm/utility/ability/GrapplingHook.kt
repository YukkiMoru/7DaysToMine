package com.github.yukkimoru.sdtm.utility.ability

import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable
import org.bukkit.util.Vector

class GrapplingHook(private val plugin: Plugin) : Listener {
	private var allowGrapple = true

	@EventHandler
	fun onPlayerFish(event: PlayerFishEvent) {
		val player = event.player

		if ((event.state == PlayerFishEvent.State.REEL_IN || event.state == PlayerFishEvent.State.IN_GROUND) && allowGrapple) {
			allowGrapple = false
			val hookLocation: Location = event.hook.location
			val playerLocation: Location = player.location

			var direction: Vector = hookLocation.toVector().subtract(playerLocation.toVector()).normalize()

			direction = direction.multiply(1.25) // 速度を調整
			direction.y = 0.5 // Y軸を固定

			player.velocity = direction
			object : BukkitRunnable() {
				override fun run() {
					allowGrapple = true
					// message to player
					player.sendMessage("§aGrappling Hook is ready!")
				}
			}.runTaskLater(plugin, 30L)
		}
	}
}