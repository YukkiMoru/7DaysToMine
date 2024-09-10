package com.github.YukkiMoru.SDTM

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin

class OreRegeneration(private val plugin: JavaPlugin) : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        if (block.type == Material.COAL_ORE || block.type == Material.IRON_ORE) {
            // block will be regenerated after 3 seconds
            val scheduler = plugin.server.scheduler
            scheduler.scheduleSyncDelayedTask(plugin, {
                block.type = Material.COAL_ORE // or block.type = Material.IRON_ORE
            }, 60L)
        }
    }
}