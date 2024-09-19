package com.github.YukkiMoru.SDTM.CORE

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin
import java.io.File

class ScanBlocks {

	private val spawnerLocations = mutableListOf<Map<String, Int>>()

	fun scan(plugin: JavaPlugin, x: Int, y: Int, z: Int) {
		val world = plugin.server.getWorld("world") ?: return
		val center = Location(world, x.toDouble(), y.toDouble(), z.toDouble())

		for (dx in -100..100) {
			for (dy in -100..100) {
				for (dz in -100..100) {
					val loc = center.clone().add(dx.toDouble(), dy.toDouble(), dz.toDouble())
					val block = loc.block
					if (block.type == Material.LIGHT) {
						plugin.logger.info("Spawner found at: ${loc.blockX}, ${loc.blockY}, ${loc.blockZ}")
						spawnerLocations.add(mapOf("x" to loc.blockX, "y" to loc.blockY, "z" to loc.blockZ))
					}
				}
			}
		}
		saveToJson()
	}

	private fun saveToJson() {
		val gson: Gson = GsonBuilder().setPrettyPrinting().create()
		val json = gson.toJson(spawnerLocations)
		val file = File("plugins/7DaysToMine/spawner_locations.json")
		file.writeText(json)
	}
}