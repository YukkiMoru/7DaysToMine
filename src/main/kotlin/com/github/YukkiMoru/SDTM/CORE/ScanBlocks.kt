package com.github.YukkiMoru.SDTM.CORE

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.plugin.java.JavaPlugin

class ScanBlocks {

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
					}
				}
			}
		}
	}
}