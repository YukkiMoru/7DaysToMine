package com.github.YukkiMoru.SDTM

//import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.plugin.java.JavaPlugin

class OreRegeneration(private val plugin: JavaPlugin) : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        val player = event.player

        if (block.type == Material.COAL_ORE || block.type == Material.IRON_ORE) {
            val originalType = block.type
            val scheduler = plugin.server.scheduler
            scheduler.scheduleSyncDelayedTask(plugin, {
                block.type = originalType
            }, 60L)

//            player.sendMessage(Component.text("The ore will regenerate in 3 seconds!"))
            // you broke the ore
//            player.sendMessage(Component.text("You broke the " + block.type + " ore!"))
        }
    }
}