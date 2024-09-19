package com.github.yukkimoru.sdtm.core

import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.attribute.Attribute
import org.bukkit.entity.EntityType
import org.bukkit.entity.Mob
import org.bukkit.inventory.ItemStack
import org.bukkit.metadata.FixedMetadataValue
import org.bukkit.plugin.java.JavaPlugin
import kotlin.random.Random

class SpawnMobs(private val plugin: JavaPlugin) {

	fun spawnHorde(level: Int) {
		val scanBlocks = ScanBlocks()
		val spawnerLocations = scanBlocks.loadFromJson()
		val world = plugin.server.getWorld("world") ?: return

		val totalMobs = level * 10
		val mobsPerLocation = totalMobs / spawnerLocations.size
		val extraMobs = totalMobs % spawnerLocations.size

		spawnerLocations.forEachIndexed { index, loc ->
			val location = Location(world, loc["x"]!!.toDouble(), loc["y"]!!.toDouble(), loc["z"]!!.toDouble())
			val mobsToSpawn = mobsPerLocation + if (index < extraMobs) 1 else 0
			val mobTypes = listOf(
				EntityType.ZOMBIE,
				EntityType.SKELETON,
				EntityType.SPIDER,
				EntityType.WITCH,
				EntityType.BLAZE
			)
			for (i in 1..mobsToSpawn) {
				val mobType = mobTypes[Random.nextInt(mobTypes.size)]
				when (mobType) {
					EntityType.ZOMBIE -> spawnCustomMob(
						location,
						mobType,
						"weakzombie",
						20.0,
						ItemStack(Material.IRON_SWORD),
						ItemStack(Material.IRON_HELMET),
						ItemStack(Material.IRON_CHESTPLATE),
						ItemStack(Material.IRON_LEGGINGS),
						ItemStack(Material.IRON_BOOTS)
					)

					EntityType.SKELETON -> spawnCustomMob(
						location,
						mobType,
						"archerskeleton",
						20.0,
						ItemStack(Material.BOW),
						ItemStack(Material.CHAINMAIL_HELMET),
						ItemStack(Material.CHAINMAIL_CHESTPLATE),
						ItemStack(Material.CHAINMAIL_LEGGINGS),
						ItemStack(Material.CHAINMAIL_BOOTS)
					)

					EntityType.SPIDER -> spawnCustomMob(
						location,
						mobType,
						"fastspider",
						16.0,
						null,
						null,
						null,
						null,
						null
					)

					EntityType.WITCH -> spawnCustomMob(
						location,
						mobType,
						"dangerouswitch",
						26.0,
						null,
						null,
						null,
						null,
						null
					)

					EntityType.BLAZE -> spawnCustomMob(
						location,
						mobType,
						"fieryblaze",
						20.0,
						null,
						null,
						null,
						null,
						null
					)

					else -> {}
				}
			}
		}
	}

	private fun spawnCustomMob(
		location: Location,
		entityType: EntityType,
		metadataKey: String,
		health: Double,
		weapon: ItemStack?,
		helmet: ItemStack?,
		chestplate: ItemStack?,
		leggings: ItemStack?,
		boots: ItemStack?
	) {
		val world = location.world ?: return
		val mob = world.spawnEntity(location, entityType) as? Mob ?: return

		mob.setMetadata(metadataKey, FixedMetadataValue(plugin, true))
		mob.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = health
		mob.health = health

		weapon?.let { mob.equipment?.setItemInMainHand(it) }
		helmet?.let { mob.equipment?.helmet = it }
		chestplate?.let { mob.equipment?.chestplate = it }
		leggings?.let { mob.equipment?.leggings = it }
		boots?.let { mob.equipment?.boots = it }
	}
}