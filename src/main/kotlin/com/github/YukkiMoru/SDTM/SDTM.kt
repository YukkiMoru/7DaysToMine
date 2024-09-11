package com.github.YukkiMoru.SDTM

import com.github.YukkiMoru.SDTM.CORE.NexusScoreboard
import com.github.YukkiMoru.SDTM.TRADE.ItemShop
import com.github.YukkiMoru.SDTM.UTILITY.SDCommand
import com.github.YukkiMoru.SDTM.UTILITY.protectVillager
import com.github.YukkiMoru.SDTM.WORLD.MobController
import com.github.YukkiMoru.SDTM.WORLD.OreRegeneration
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.plugin.java.JavaPlugin

import org.bukkit.configuration.file.YamlConfiguration
import java.io.File

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

        // Summon custom villager
        val itemShop = ItemShop(this)
        val location = Location(Bukkit.getWorld("world"), 95.5, 10.0, 91.5)
        itemShop.summonCustomVillager(location)

        // Summon block_display entity
        val summonCommand = "summon block_display 100.1 11.1 100.1 {transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],scale:[0.8f,0.8f,0.8f],translation:[0f,0f,0f]},block_state:{Name:end_stone}}"
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), summonCommand)


        server.pluginManager.registerEvents(protectVillager(this), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}