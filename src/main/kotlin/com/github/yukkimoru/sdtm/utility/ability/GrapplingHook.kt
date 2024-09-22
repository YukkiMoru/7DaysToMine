package com.github.yukkimoru.sdtm.utility.ability

import org.bukkit.Location
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.util.Vector

class GrapplingHook : Listener {

	@EventHandler
	fun onPlayerFish(event: PlayerFishEvent) {
		val player = event.player
		if (event.state == PlayerFishEvent.State.REEL_IN || event.state == PlayerFishEvent.State.IN_GROUND) {
			val hookLocation: Location = event.hook.location
			val playerLocation: Location = player.location

			val direction: Vector = hookLocation.toVector().subtract(playerLocation.toVector()).normalize()
			direction.y = 0.5 // Y軸を固定

			player.velocity = direction.multiply(2)
		}
	}
}