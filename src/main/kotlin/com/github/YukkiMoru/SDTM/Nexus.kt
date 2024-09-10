package com.github.YukkiMoru.SDTM

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.scoreboard.DisplaySlot
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Scoreboard

class NexusScoreboard {

    private val scoreboard: Scoreboard = Bukkit.getScoreboardManager().newScoreboard
    private val objective: Objective
    private var nexusHealth: Int = 10000

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
}