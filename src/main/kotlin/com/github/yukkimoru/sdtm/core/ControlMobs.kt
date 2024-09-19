package com.github.yukkimoru.sdtm.core

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.entity.Villager
import org.bukkit.plugin.java.JavaPlugin

class ControlMobs(private val plugin: JavaPlugin, private val nexusScoreboard: NexusScoreboard) {

	private val targetLocation = Location(Bukkit.getWorld("world"), 100.1, 11.1, 100.1)

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
				val nearestPlayer = world.getNearbyEntities(entity.location, 10.0, 10.0, 10.0)
					.filterIsInstance<Player>()
					.minByOrNull { it.location.distance(entity.location) }

				if (nearestPlayer != null) {
					entity.pathfinder.moveTo(nearestPlayer.location)
				} else {
					entity.pathfinder.moveTo(targetLocation)
				}
			}
		}
	}
}