package com.github.YukkiMoru.SDTM.CORE

import org.bukkit.Location
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin

class SpawnMobs(private val plugin: JavaPlugin) {

	fun spawnHorde(level: Int) {
		val scanBlocks = ScanBlocks()
		val spawnerLocations = scanBlocks.loadFromJson()
		val world = plugin.server.getWorld("world") ?: return

		val totalZombies = level * 10
		val zombiesPerLocation = totalZombies / spawnerLocations.size
		val extraZombies = totalZombies % spawnerLocations.size

		spawnerLocations.forEachIndexed { index, loc ->
			val location = Location(world, loc["x"]!!.toDouble(), loc["y"]!!.toDouble(), loc["z"]!!.toDouble())
			val zombiesToSpawn = zombiesPerLocation + if (index < extraZombies) 1 else 0
			for (i in 1..zombiesToSpawn) {
				val zombie = world.spawnEntity(location, EntityType.ZOMBIE) as? Mob
				zombie?.setMetadata("weakzombie", FixedMetadataValue(plugin, true))
			}
		}
	}
}