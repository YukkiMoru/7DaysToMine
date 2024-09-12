package com.github.YukkiMoru.SDTM.WORLD

import com.github.YukkiMoru.SDTM.CORE.NexusScoreboard
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Mob
import org.bukkit.entity.Villager
import org.bukkit.entity.Zombie
import org.bukkit.plugin.java.JavaPlugin

class MobController(private val plugin: JavaPlugin, private val nexusScoreboard: NexusScoreboard) {

    private val targetLocation = Location(Bukkit.getWorld("world"), 100.1, 11.1, 100.1)

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
            if (entity is Mob && entity !is Villager) {
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