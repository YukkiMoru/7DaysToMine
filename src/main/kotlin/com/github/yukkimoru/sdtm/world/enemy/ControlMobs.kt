package com.github.yukkimoru.sdtm.world.enemy

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.plugin.java.JavaPlugin

class ControlMobs(private val plugin: JavaPlugin) {

	private val targetLocation = Location(Bukkit.getWorld("world"), 100.1, 11.1, 100.1)
	private val mobTargets = mutableMapOf<Mob, Player>()

	fun startMovingMobsToLocation() {
		val scheduler = plugin.server.scheduler
		scheduler.scheduleSyncRepeatingTask(plugin, {
			moveMobsToLocation()
		}, 0L, 20L) // 20 ticks = 1 second
	}

	private fun moveMobsToLocation() {
		val world = targetLocation.world

		world?.entities?.forEach { entity ->
			if (entity is Mob && entity !is Villager) {
				val currentTarget = mobTargets[entity]
				if (currentTarget != null && currentTarget.isOnline && !currentTarget.isDead) {
					entity.pathfinder.moveTo(currentTarget.location)
				} else {
					val nearestPlayer = world.getNearbyEntities(entity.location, 10.0, 10.0, 10.0)
						.filterIsInstance<Player>()
						.minByOrNull { it.location.distance(entity.location) }

					if (nearestPlayer != null) {
						mobTargets[entity] = nearestPlayer
						entity.pathfinder.moveTo(nearestPlayer.location)
					} else {
						entity.pathfinder.moveTo(targetLocation)
					}
				}
			}
		}
	}
}