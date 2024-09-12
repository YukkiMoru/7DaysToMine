package com.github.YukkiMoru.SDTM.UTILITY

import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityTargetLivingEntityEvent
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

  //村人を襲わない(ただしラグが見込まれるので禁止)
//	@EventHandler
//	fun onEntityTargetLivingEntity(event: EntityTargetLivingEntityEvent) {
//		val target = event.target
//		val entity = event.entity
//
//		if (target?.type == EntityType.VILLAGER && entity.type == EntityType.ZOMBIE) {
//			event.isCancelled = true
//		}
//	}
}