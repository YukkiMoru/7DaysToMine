package com.github.YukkiMoru.SDTM.WORLD

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class RegenerateOres(private val plugin: JavaPlugin) : Listener {

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        val block = event.block
        val player = event.player
        val itemInHand: ItemStack = player.inventory.itemInMainHand

        // Check if the player is breaking the block with an empty hand
        if (itemInHand.type == Material.AIR) {
            event.isCancelled = true
            return
        }

        // Check if the item in hand is the special pickaxe
        if (itemInHand.type == Material.IRON_PICKAXE && itemInHand.itemMeta?.isUnbreakable == true) {
            val container = itemInHand.itemMeta?.persistentDataContainer
            val key = NamespacedKey(plugin, "destroyable_blocks")
            val destroyableBlocks = container?.get(key, PersistentDataType.STRING)?.split(",") ?: emptyList()
            if (destroyableBlocks.contains(block.type.key.toString())) {
                // Allow block break and regenerate the block
                val originalType = block.type
                val scheduler = plugin.server.scheduler
                scheduler.scheduleSyncDelayedTask(plugin, {
                    block.type = Material.BEDROCK
                }, 1L)
                scheduler.scheduleSyncDelayedTask(plugin, {
                    block.type = originalType
                }, 60L)
                return
            } else {
                event.isCancelled = true
            }
        } else {
            // Cancel the event if the block is not breakable by the special pickaxe
            if (block.type == Material.COAL_ORE || block.type == Material.IRON_ORE) {
                event.isCancelled = true
            }
        }
    }
}