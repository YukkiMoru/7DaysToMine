package com.github.yukkimoru.sdtm.trade

import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.entity.EntityTargetLivingEntityEvent

class ProtectVillagers : Listener {
	//村人を攻撃させない
	@EventHandler
	fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
		val entity = event.entity
		val damager = event.damager

		if (entity.type == EntityType.VILLAGER && damager is Player) {
			event.isCancelled = true
		}
	}

	//村人を襲わないようにする
	@EventHandler
	fun onEntityTargetLivingEntity(event: EntityTargetLivingEntityEvent) {
		val target = event.target
		val entity = event.entity

		if (target?.type == EntityType.VILLAGER && entity.type == EntityType.ZOMBIE) {
			event.isCancelled = true
		}
	}
}