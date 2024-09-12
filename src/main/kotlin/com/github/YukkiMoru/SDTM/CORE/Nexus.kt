package com.github.YukkiMoru.SDTM.CORE

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.Particle
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.util.Vector
import org.bukkit.entity.Zombie

class NexusScoreboard {

    private val scoreboard: Scoreboard = Bukkit.getScoreboardManager().newScoreboard
    private val objective: Objective
    private var nexusHealth: Int = 10000
    private val targetLocation = Location(Bukkit.getWorld("world"), 100.0, 10.1, 100.0)

    init {
        objective = scoreboard.registerNewObjective("nexusHealth", "dummy", "${ChatColor.GREEN}Nexus Health")
        objective.displaySlot = DisplaySlot.SIDEBAR
        updateNexusHealth(nexusHealth)
    }

    fun updateNexusHealth(health: Int) {
        nexusHealth = health
        objective.getScore("${ChatColor.RED}Nexus Health:").score = nexusHealth
    }

    fun getNexusHealth(): Int {
        return nexusHealth
    }

    fun getScoreboard(): Scoreboard {
        return scoreboard
    }

    fun checkZombiesNearNexus() {
        val world = targetLocation.world

        // ネクサスを中心とした半径5ブロックの円を描画する
//        val radius = 5.0
//        val points = 100
//        for (i in 0 until points) {
//            val angle = 2 * Math.PI * i / points
//            val x = radius * Math.cos(angle)
//            val z = radius * Math.sin(angle)
//            val particleLocation = targetLocation.clone().add(Vector(x+0.5, 0.0, z+0.5))
//            world?.spawnParticle(Particle.COMPOSTER, particleLocation, 1)
//        }

    }
}