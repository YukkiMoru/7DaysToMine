package com.github.YukkiMoru.SDTM

import org.bukkit.plugin.java.JavaPlugin

class SDTM : JavaPlugin() {

    override fun onEnable() {
        // Plugin startup logic
        // message on console
        logger.info("SDTM plugin enabled")
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}
