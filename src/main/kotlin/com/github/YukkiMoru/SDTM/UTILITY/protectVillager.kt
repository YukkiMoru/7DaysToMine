package com.github.YukkiMoru.SDTM.UTILITY

import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.plugin.java.JavaPlugin

class protectVillager(private val plugin: JavaPlugin) : Listener {
  @EventHandler
  fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
	val entity = event.entity
	val damager = event.damager

	if (entity.type == EntityType.VILLAGER && damager is Player) {
	  event.isCancelled = true
	}
  }
}