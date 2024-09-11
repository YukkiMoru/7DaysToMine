package com.github.YukkiMoru.SDTM

import com.github.YukkiMoru.SDTM.CORE.NexusScoreboard
import com.github.YukkiMoru.SDTM.TRADE.LocateVillagers
import com.github.YukkiMoru.SDTM.UTILITY.SDCommand
import com.github.YukkiMoru.SDTM.UTILITY.protectVillager
import com.github.YukkiMoru.SDTM.WORLD.MobController
import com.github.YukkiMoru.SDTM.WORLD.OreRegeneration
import org.bukkit.Bukkit
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

        // Summon custom villagers using SummonVillagers
        val summonVillagers = LocateVillagers(this)
        server.pluginManager.registerEvents(summonVillagers, this)
        summonVillagers.summonVillagers() // Call the function to summon villagers

        // Summon block_display entity
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "summon block_display 100.1 11.1 100.1 {transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],scale:[0.8f,0.8f,0.8f],translation:[0f,0f,0f]},block_state:{Name:end_stone}}")

        // kill all entities expect players
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=!player]")

        server.pluginManager.registerEvents(protectVillager(this), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}