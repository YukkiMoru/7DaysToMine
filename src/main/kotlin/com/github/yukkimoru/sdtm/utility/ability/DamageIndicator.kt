package com.github.yukkimoru.sdtm.utility.ability

import net.kyori.adventure.text.Component
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
			val finalDamage = event.finalDamage
			showDamageIndicator(entity.location.add(0.0, 1.0, 0.0), finalDamage)
		}
	}

	private fun showDamageIndicator(location: Location, damage: Double) {
		val world = location.world ?: return
		val armorStand = world.spawnEntity(location, EntityType.ARMOR_STAND) as ArmorStand

		armorStand.isVisible = false
		armorStand.isCustomNameVisible = true
		armorStand.customName(Component.text("§c-${damage.toInt()}"))
		armorStand.isMarker = true
		armorStand.setGravity(false)

		object : BukkitRunnable() {
			override fun run() {
				armorStand.remove()
			}
		}.runTaskLater(plugin, 40L) // Remove after 2 seconds (40 ticks)
	}
}