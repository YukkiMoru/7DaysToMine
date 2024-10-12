package com.github.yukkimoru.sdtm.utility.ability

import org.bukkit.Bukkit
import org.bukkit.boss.BarColor
import org.bukkit.boss.BarStyle
import org.bukkit.boss.BossBar
import org.bukkit.entity.Entity
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

@Suppress("unused")
class HealthIndicator(private val plugin: JavaPlugin) {

	private val bossBars = mutableMapOf<Player, BossBar>()

	fun startHealthIndicator() {
		object : BukkitRunnable() {
			override fun run() {
				for (player in Bukkit.getOnlinePlayers()) {
					val target = getTargetedEntity(player)
					if (target is Mob) {
						val health = target.health
						val maxHealth =
							target.getAttribute(org.bukkit.attribute.Attribute.GENERIC_MAX_HEALTH)?.value ?: 20.0
						val bossBar = bossBars.computeIfAbsent(player) {
							Bukkit.createBossBar("Mob Health", BarColor.RED, BarStyle.SOLID).apply {
								addPlayer(player)
							}
						}
						bossBar.progress = health / maxHealth
						bossBar.setTitle("${target.name}: ${health.toInt()}/${maxHealth.toInt()}")
					} else {
						bossBars[player]?.removeAll()
						bossBars.remove(player)
					}
				}
			}
		}.runTaskTimer(plugin, 0L, 1L)
	}

	private fun getTargetedEntity(player: Player): Entity? {
		val rayTraceResult = player.getTargetEntity(20)
		return rayTraceResult
	}
}