package com.github.YukkiMoru.SDTM.WORLD

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.attribute.AttributeModifier
import org.bukkit.attribute.AttributeModifier.Operation
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.*

class RegenerateOres(private val plugin: JavaPlugin) : Listener {

    private val playerTargetBlocks = mutableMapOf<UUID, Material?>()

    init {
        // Schedule a repeating task to update the target block display
        object : BukkitRunnable() {
            override fun run() {
                for (player in Bukkit.getOnlinePlayers()) {
                    val targetBlock = player.getTargetBlockExact(5) // Get the block the player is looking at within 5 blocks
                    val previousBlock = playerTargetBlocks[player.uniqueId]

                    if (targetBlock != null && targetBlock.type != Material.AIR) {
                        player.sendActionBar("Target Block: ${targetBlock.type}")
                    } else {
                        player.sendActionBar("No target block")
                    }

                    if (targetBlock?.type != previousBlock) {
                        playerTargetBlocks[player.uniqueId] = targetBlock?.type

                        val itemInHand: ItemStack = player.inventory.itemInMainHand
                        if (itemInHand.type == Material.IRON_PICKAXE && itemInHand.itemMeta?.isUnbreakable == true) {
                            val container = itemInHand.itemMeta?.persistentDataContainer
                            val key = NamespacedKey(plugin, "destroyable_blocks")
                            val destroyableBlocks = container?.get(key, PersistentDataType.STRING)?.split(",") ?: emptyList<String>()
                            if (targetBlock != null && destroyableBlocks.contains(targetBlock.type.key.toString())) {
                                setBlockBreakSpeed(player, 1.0)
                            } else {
                                setBlockBreakSpeed(player, 0.0)
                            }
                        } else {
                            setBlockBreakSpeed(player, 0.0)
                        }
                    }
                }
            }
        }.runTaskTimer(plugin, 0L, 5L) // Run every 5 ticks (0.25 seconds)
    }

    private fun setBlockBreakSpeed(player: Player, speed: Double) {
        val attribute = player.getAttribute(Attribute.PLAYER_BLOCK_BREAK_SPEED)
        attribute?.modifiers?.forEach { attribute.removeModifier(it) }
        val modifier = AttributeModifier(NamespacedKey(plugin, "block_break_speed"), speed, Operation.ADD_NUMBER)
        attribute?.addModifier(modifier)
    }
}