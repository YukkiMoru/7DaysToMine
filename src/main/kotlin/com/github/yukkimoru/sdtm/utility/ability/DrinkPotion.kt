package com.github.yukkimoru.sdtm.utility.ability

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.attribute.Attribute
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerItemConsumeEvent
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.plugin.Plugin
import org.bukkit.persistence.PersistentDataType
import kotlin.math.min

class DrinkPotion(private val plugin: Plugin) : Listener {

    @EventHandler
    fun onPlayerDrink(event: PlayerItemConsumeEvent) {
        val player = event.player
        val item = event.item

        if (item.type == Material.POTION) {
            val meta = item.itemMeta as PotionMeta
            val container = meta.persistentDataContainer
            val potionID = container.get(NamespacedKey(plugin, "PotionID"), PersistentDataType.INTEGER)

            if (potionID != null) {
                when (potionID) {
                    1 -> {
                        // Healing Potion
                        player.health = min(player.health + 4, player.getAttribute(Attribute.GENERIC_MAX_HEALTH)!!.value)
                        player.sendMessage("§aYou drank a Healing Potion!")
                    }
                    2 -> {
                        // Strength Potion
                        // 効果を追加
                        player.sendMessage("§aYou drank a Strength Potion!")
                    }
                    3 -> {
                        // Speed Potion
                        // 効果を追加
                        player.sendMessage("§aYou drank a Speed Potion!")
                    }
                    else -> {
                        player.sendMessage("§cUnknown PotionID: $potionID")
                    }
                }
            } else {
                player.sendMessage("§cPotionID not found.")
            }
        } else {
            player.sendMessage("§cYou drank a non-potion item.")
        }
    }
}