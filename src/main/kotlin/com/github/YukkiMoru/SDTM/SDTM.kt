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
// COMMANDS
        // プレイヤー以外のエンティティを全て削除
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=!player]")
        // ネクサスのエンドストーンを召喚
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(),
            "summon block_display 100.1 11.1 100.1 {transformation:{left_rotation:[0f,0f,0f,1f],right_rotation:[0f,0f,0f,1f],scale:[0.8f,0.8f,0.8f],translation:[0f,0f,0f]},block_state:{Name:end_stone}}")
// CORE
        val nexusScoreboard = NexusScoreboard()
        server.onlinePlayers.forEach { player ->
            player.scoreboard = nexusScoreboard.getScoreboard()
        }
// TRADE
        val summonVillagers = LocateVillagers(this)
        server.pluginManager.registerEvents(summonVillagers, this)
        summonVillagers.summonVillagers()
// UTILITY
        server.pluginManager.registerEvents(protectVillager(this), this)

        SDCommand(this).registerCommands()

// WORLD
        val mobController = MobController(this, nexusScoreboard)
        mobController.startMovingMobsToLocation()

        server.scheduler.scheduleSyncRepeatingTask(this, {
            nexusScoreboard.checkZombiesNearNexus()
        }, 0L, 2L) // 2 ticks = 0.1 second

        server.pluginManager.registerEvents(OreRegeneration(this), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}