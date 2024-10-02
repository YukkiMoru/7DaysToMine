package com.github.yukkimoru.sdtm.utility.ability

import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.plugin.Plugin
import org.bukkit.persistence.PersistentDataType
import org.bukkit.scheduler.BukkitTask
import kotlin.math.min

class DrinkPotion(private val plugin: Plugin) : Listener {

    private var potionEffectTask: BukkitTask? = null

    @EventHandler
    fun onPlayerDrink(event: PlayerItemConsumeEvent) {
        val player = event.player
        val item = event.item

        if (item.type == Material.POTION) {
            val meta = item.itemMeta as PotionMeta
            val container = meta.persistentDataContainer
            val potionID = container.get(NamespacedKey(plugin, "PotionID"), PersistentDataType.INTEGER)
            val duration = container.get(NamespacedKey(plugin, "duration"), PersistentDataType.INTEGER) ?: 0

            if (potionID != null) {
                when (potionID) {
                    1 -> {
                        // Healing Potion
                        player.health = min(player.health + 4, player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value)
                        player.sendMessage("§aYou drank a Healing Potion!")
                    }
                    2 -> {
                        // Strength Potion
                        player.sendMessage("§aYou drank a Strength Potion!")
                    }
                    3 -> {
                        // Speed Potion
                        player.sendMessage("§aYou drank a Speed Potion!")
                    }
                    4 -> {
                        // Giant Potion
                        player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = 2.0
                        player.sendMessage("§aYou drank a Giant Potion!")
                    }
                    5 -> {
                        // Midget Potion
                        player.getAttribute(Attribute.GENERIC_SCALE)?.baseValue = 1.0
                        player.sendMessage("§aYou drank a Midget Potion!")
                    }
                    else -> {
                        player.sendMessage("§cUnknown PotionID: $potionID")
                    }
                }
                setTimer(player, potionID, duration)
            } else {
                player.sendMessage("§cPotionID not found.")
            }
        } else {
            player.sendMessage("§cYou drank a non-potion item.")
        }
    }

    /**
     * Sets a timer for the potion effect.
     * @param player The player who drank the potion.
     * @param potionID The ID of the potion effect.
     * @param duration The duration of the potion effect in seconds.
     */
    private fun setTimer(player: Player, potionID: Int, duration: Int) {
        potionEffectTask = Bukkit.getScheduler().runTaskLater(plugin, Runnable {
            removeTimer(player, potionID)
        }, duration * 20L)
    }

    /**
     * Removes the timer for the potion effect.
     * @param player The player whose potion effect timer is to be removed.
     * @param potionID The ID of the potion effect.
     */
    private fun removeTimer(player: Player, potionID: Int) {
        potionEffectTask?.cancel()
        potionEffectTask = null
        player.sendMessage("§cThe effect of potion $potionID has worn off.")
    }
}