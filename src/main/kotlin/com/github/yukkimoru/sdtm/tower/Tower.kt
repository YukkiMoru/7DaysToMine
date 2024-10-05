package com.github.yukkimoru.sdtm.tower

import org.bukkit.entity.ArmorStand
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Monster
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitTask

class Tower(
	private var armorStand: ArmorStand,
	private val damage: Double,
	private val fireRate: Long,
	private val range: Double,
	private val TowerID: Int,
	private val plugin: JavaPlugin
) {
	private val attackTask: BukkitTask =
		plugin.server.scheduler.runTaskTimer(plugin, Runnable { attackMobs() }, 0L, fireRate)

	init {
		armorStand.customName = TowerID.toString()
		armorStand.isCustomNameVisible = true
		towers[TowerID] = this
	}

	fun getArmorStand(): ArmorStand {
		return armorStand
	}

	fun updateArmorStand(newArmorStand: ArmorStand) {
		armorStand.remove()
		armorStand = newArmorStand
	}

	fun cancelAttackTask() {
		attackTask.cancel()
	}

	private fun attackMobs() {
		val nearbyEntities = armorStand.getNearbyEntities(range, range, range)
		for (entity in nearbyEntities) {
			if (entity is Monster) {
				shootArrow(entity)
				break
			}
		}
	}

	private fun shootArrow(target: LivingEntity) {
		val loc = armorStand.eyeLocation.clone()
		val targetLocation = target.location.clone().apply { y += target.height * 0.9 }
		val direction = targetLocation.subtract(loc).toVector()
		val arrow = armorStand.world.spawnArrow(loc, direction, damage.toFloat(), 0f)
		arrow.shooter = armorStand
		arrow.damage = damage
	}

	companion object {
		private val towers = mutableMapOf<Int, Tower>()

		fun getTowerById(TowerID: Int): Tower? {
			return towers[TowerID]
		}

		fun removeTowerStand(TowerID: Int) {
			val tower = getTowerById(TowerID)
			tower?.let {
				for (entity in it.armorStand.world.entities) {
					if (entity is ArmorStand && entity.customName == TowerID.toString()) {
						entity.remove()
						it.cancelAttackTask()
						towers.remove(TowerID)
						break
					}
				}
			}
		}
	}
}