package com.github.yukkimoru.sdtm.utility.commands

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
		val mobTypes = listOf(
			EntityType.ZOMBIE,
			EntityType.SKELETON,
			EntityType.SPIDER,
			EntityType.WITCH,
			EntityType.BLAZE
		)

		for (i in 1..totalMobs) {
			val randomLocation = spawnerLocations.random()
			val location = Location(
				world,
				randomLocation["x"]!!.toDouble(),
				randomLocation["y"]!!.toDouble(),
				randomLocation["z"]!!.toDouble()
			)
			when (val mobType = mobTypes[Random.nextInt(mobTypes.size)]) {
				EntityType.ZOMBIE -> spawnCustomMob(
					location,
					mobType,
					"weak zombie",
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
					"archer skeleton",
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
					"fast spider",
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
					"dangerous witch",
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
					"fiery blaze",
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

	private fun spawnCustomMob(
		location: Location,
		entityType: EntityType,
		metadataKey: String,
		health: Double,
		weapon: ItemStack?,
		helmet: ItemStack?,
		chestPlate: ItemStack?,
		leggings: ItemStack?,
		boots: ItemStack?
	) {
		val world = location.world ?: return
		val mob = world.spawnEntity(location, entityType) as? Mob ?: return

		mob.setMetadata(metadataKey, FixedMetadataValue(plugin, true))
		mob.getAttribute(Attribute.GENERIC_MAX_HEALTH)?.baseValue = health
		mob.health = health

		weapon?.let { mob.equipment.setItemInMainHand(it) }
		helmet?.let { mob.equipment.helmet = it }
		chestPlate?.let { mob.equipment.chestplate = it }
		leggings?.let { mob.equipment.leggings = it }
		boots?.let { mob.equipment.boots = it }
	}
}