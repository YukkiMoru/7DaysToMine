package com.github.YukkiMoru.SDTM

import org.bukkit.plugin.java.JavaPlugin

class SDTM : JavaPlugin() {

    override fun onEnable() {
        logger.info("SDTM plugin enabled")

        // Register command using SDCommand class
        val sdCommand = SDCommand(this)
        sdCommand.registerCommands()

        // Register the OreRegeneration class
        server.pluginManager.registerEvents(OreRegeneration(this), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}