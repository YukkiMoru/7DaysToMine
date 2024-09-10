package com.github.YukkiMoru.SDTM

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Mob
import org.bukkit.entity.Player
import org.bukkit.entity.Zombie
import org.bukkit.plugin.java.JavaPlugin

class MobController(private val plugin: JavaPlugin) {

    private val targetLocation = Location(Bukkit.getWorld("world"), 8.0, -60.0, 61.0)

    fun startMovingMobsToLocation() {
        val scheduler = plugin.server.scheduler
        scheduler.scheduleSyncRepeatingTask(plugin, {
            moveMobsToLocation()
        }, 0L, 20L) // 20 ticks = 1 seconds
    }

    private fun moveMobsToLocation() {
        val world = targetLocation.world

        world?.entities?.forEach { entity ->
            if (entity is Mob) {
                if (entity is Zombie && entity.target is Player) {
                    return@forEach
                }
                entity.pathfinder.moveTo(targetLocation)
            }
        }
    }
}