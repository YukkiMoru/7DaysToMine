package com.github.YukkiMoru.SDTM

import com.github.YukkiMoru.SDTM.CORE.NexusScoreboard
import com.github.YukkiMoru.SDTM.UTILITY.SDCommand
import com.github.YukkiMoru.SDTM.WORLD.MobController
import com.github.YukkiMoru.SDTM.WORLD.OreRegeneration
import org.bukkit.plugin.java.JavaPlugin

class SDTM : JavaPlugin() {

    override fun onEnable() {
        logger.info("SDTM plugin enabled")

        // Register command using SDCommand class
        val sdCommand = SDCommand(this)
        sdCommand.registerCommands()

        // Register the OreRegeneration class
        server.pluginManager.registerEvents(OreRegeneration(this), this)

        // Initialize and set the NexusScoreboard
        val nexusScoreboard = NexusScoreboard()
        server.onlinePlayers.forEach { player ->
            player.scoreboard = nexusScoreboard.getScoreboard()
        }

        // Initialize MobController and start moving mobs to location
        val mobController = MobController(this, nexusScoreboard)
        mobController.startMovingMobsToLocation()

        // Schedule a repeating task to display particles around the target location
        server.scheduler.scheduleSyncRepeatingTask(this, {
            nexusScoreboard.checkZombiesNearNexus()
        }, 0L, 3L) // 20 ticks = 1 second
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}