package com.github.yukkimoru.sdtm.utility

import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.EntityType
import org.bukkit.entity.LivingEntity
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class DamageIndicator(private val plugin: JavaPlugin) : Listener {

	@EventHandler
	fun onEntityDamage(event: EntityDamageByEntityEvent) {
		val entity = event.entity
		if (entity is LivingEntity) {
			val damage = event.damage
			showDamageIndicator(entity.location, damage)
		}
	}

	private fun showDamageIndicator(location: Location, damage: Double) {
		val world = location.world ?: return
		val armorStand = world.spawnEntity(location, EntityType.ARMOR_STAND) as ArmorStand

		armorStand.isVisible = false
		armorStand.isCustomNameVisible = true
		armorStand.customName = "§c-${damage.toInt()}"
		armorStand.isMarker = true
		armorStand.setGravity(false)

		object : BukkitRunnable() {
			override fun run() {
				armorStand.remove()
			}
		}.runTaskLater(plugin, 40L) // Remove after 2 seconds (40 ticks)
	}
}