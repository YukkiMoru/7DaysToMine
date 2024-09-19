package com.github.YukkiMoru.SDTM.CORE

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.plugin.java.JavaPlugin

class SpawnMobs(private val plugin: JavaPlugin) {

	fun spawnHorde(level: Int) {
		val world = plugin.server.getWorld("world")
		val location = Location(world, 100.0, 10.0, 100.0)
		for (i in 1..(level * 10)) {
			world?.spawnEntity(location, EntityType.ZOMBIE) as? Mob
		}
	}
}