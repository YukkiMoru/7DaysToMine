package com.github.yukkimoru.sdtm.world.enemy

import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDeathEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class DropMobs(plugin: JavaPlugin) : Listener {

	init {
		plugin.server.pluginManager.registerEvents(this, plugin)
	}

	@EventHandler
	fun onMobDeath(event: EntityDeathEvent) {
		val entity = event.entity
		val drops = event.drops

		if (entity.type == EntityType.ZOMBIE && entity.hasMetadata("weakzombie")) {
			drops.clear()
			drops.add(ItemStack(Material.DIAMOND, 1))
		} else {
			when (entity.type) {
				EntityType.ZOMBIE -> {
					drops.add(ItemStack(Material.ROTTEN_FLESH, 2))
					drops.add(ItemStack(Material.IRON_INGOT, 1))
				}

				EntityType.SKELETON -> {
					drops.add(ItemStack(Material.BONE, 2))
					drops.add(ItemStack(Material.ARROW, 5))
				}
				// Add more cases for different mobs and their drops
				else -> {
					// Default drops or no additional drops
				}
			}
		}
	}
}