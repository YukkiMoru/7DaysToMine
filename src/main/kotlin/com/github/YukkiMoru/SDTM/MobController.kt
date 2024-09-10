package com.github.YukkiMoru.SDTM

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Mob
import org.bukkit.entity.Zombie
import org.bukkit.plugin.java.JavaPlugin

class MobController(private val plugin: JavaPlugin, private val nexusScoreboard: NexusScoreboard) {

    private val targetLocation = Location(Bukkit.getWorld("world"), 8.0, -60.0, 61.0)

    fun startMovingMobsToLocation() {
        val scheduler = plugin.server.scheduler
        scheduler.scheduleSyncRepeatingTask(plugin, {
            moveMobsToLocation()
            checkZombiesNearNexus()
        }, 0L, 20L) // 20 ticks = 1 second
    }

    private fun moveMobsToLocation() {
        val world = targetLocation.world

        world?.entities?.forEach { entity ->
            if (entity is Mob) {
                entity.pathfinder.moveTo(targetLocation)
            }
        }
    }

    private fun checkZombiesNearNexus() {
        val world = targetLocation.world

        world?.entities?.forEach { entity ->
            if (entity is Zombie) {
                if (entity.location.distance(targetLocation) <= 5) {
                    val currentHealth = nexusScoreboard.getNexusHealth()
                    nexusScoreboard.updateNexusHealth(currentHealth - 1)
                }
            }
        }
    }
}