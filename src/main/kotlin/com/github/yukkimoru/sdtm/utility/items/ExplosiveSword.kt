package com.github.yukkimoru.sdtm.utility.items

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.event.player.PlayerItemHeldEvent
import org.bukkit.plugin.java.JavaPlugin

class ExplosiveSword(private val plugin: JavaPlugin) : Listener {

	private val cooldown = 5000L // 5 seconds
	private val itemLib = ItemLib(plugin)
	private val isHoldItem = false
	private var isCooldown = false
	private val debugMode = true

	@EventHandler
	fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
		val attacker = event.damager
		val entity = event.entity
		if (isHoldItem&& !isCooldown) {
			entity.world.createExplosion(entity.location, 2.0f, false, false)
			attacker.velocity = attacker.location.direction.multiply(-1)
			isCooldown = true
			itemLib.delayTick(cooldown / 50) {
				if (debugMode) attacker.sendMessage("§a爆発県の特殊能力のクールダウンが終わりました")
				isCooldown = false
			}
		} else {
			if (debugMode) attacker.sendMessage("§c爆発県の特殊能力のクールダウン中です")
		}
	}

	@EventHandler
	fun onPlayerItemHeld(event: PlayerItemHeldEvent) {
		val player = event.player
		val itemLib = ItemLib(plugin)
		var holdItem = false
		val cooldown = 5000L // 5 seconds

		itemLib.delayTick(1L) {
			holdItem = itemLib.isHoldingItem(player, 302)
			if (holdItem) {
				if (debugMode) player.sendMessage("§a爆発剣を装備しました")
				itemLib.runWithCooldown(cooldown / 50) {
					if (debugMode) player.sendMessage("§a爆発剣の特殊能力が使用可能")
					isCooldown = false
				}
			} else {
				if (debugMode) player.sendMessage("§c爆発剣の特殊能力が無効化されました")
				isCooldown = true
			}
		}
	}
}