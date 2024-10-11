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

    private fun startPotionEffectTimer(player: Player, potionEffectInstance: PotionEffectInstance) {
        object : BukkitRunnable() {
            override fun run() {
                playerPotions[player]?.remove(potionEffectInstance)
            }
        }.runTaskLater(plugin, potionEffectInstance.duration * 20L)
    }

    data class PotionEffectInstance(val potionID: Int, val duration: Int)
}