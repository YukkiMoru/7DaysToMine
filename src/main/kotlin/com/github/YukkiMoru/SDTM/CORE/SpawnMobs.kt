package com.github.YukkiMoru.SDTM.CORE

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.plugin.java.JavaPlugin

class SpawnMobs(private val plugin: JavaPlugin) {

	fun spawnHorde(level: Int) {
		val scanBlocks = ScanBlocks()
		val spawnerLocations = scanBlocks.loadFromJson()
		val world = plugin.server.getWorld("world")

		spawnerLocations.forEach { loc ->
			val location = Location(world, loc["x"]!!.toDouble(), loc["y"]!!.toDouble(), loc["z"]!!.toDouble())
			for (i in 1..(level * 10)) {
				world?.spawnEntity(location, EntityType.ZOMBIE) as? Mob
			}
		}
	}
}