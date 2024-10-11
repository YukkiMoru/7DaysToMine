package com.github.yukkimoru.sdtm.utility.potions

import org.bukkit.entity.Player
import org.bukkit.plugin.Plugin
import org.bukkit.scheduler.BukkitRunnable

class PotionList(private val plugin: Plugin) {
    private val playerPotions = mutableMapOf<Player, MutableList<PotionEffectInstance>>()

    fun addPotion(player: Player, potionID: Int, duration: Int) {
        val potionEffectInstance = PotionEffectInstance(potionID, duration)
        playerPotions.computeIfAbsent(player) { mutableListOf() }.add(potionEffectInstance)
        startPotionEffectTimer(player, potionEffectInstance)
    }

    fun removePlayer(player: Player) {
        playerPotions.remove(player)
    }

    fun hasActiveCooldown(player: Player, potionID: Int): Boolean {
        val potions = playerPotions[player] ?: return false
        return potions.any { it.potionID == potionID && it.duration > 0 }
    }

    private fun startPotionEffectTimer(player: Player, potionEffectInstance: PotionEffectInstance) {
        object : BukkitRunnable() {
            override fun run() {
                potionEffectInstance.duration -= 1
                if (potionEffectInstance.duration <= 0) {
                    playerPotions[player]?.remove(potionEffectInstance)
                    cancel()
                }
            }
        }.runTaskTimer(plugin, 0L, 20L)
    }

    data class PotionEffectInstance(val potionID: Int, var duration: Int)
}