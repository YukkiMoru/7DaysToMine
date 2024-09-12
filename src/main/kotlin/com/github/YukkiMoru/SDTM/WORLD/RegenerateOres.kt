package com.github.YukkiMoru.SDTM.WORLD

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

class RegenerateOres(private val plugin: JavaPlugin) : Listener {


    init {
        // Schedule a repeating task to update the target block display
        object : BukkitRunnable() {
            override fun run() {
                for (player in Bukkit.getOnlinePlayers()) {
                    val targetBlock = player.getTargetBlockExact(5) // Get the block the player is looking at within 5 blocks
                    if (targetBlock != null && targetBlock.type != Material.AIR) {
                        player.sendActionBar("Target Block: ${targetBlock.type}")
                    } else {
                        player.sendActionBar("No target block")
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 1L) // Run every second (20 ticks)
    }
}