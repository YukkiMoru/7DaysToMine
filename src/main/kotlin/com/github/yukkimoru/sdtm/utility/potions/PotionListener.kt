package com.github.yukkimoru.sdtm.utility.potions

import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerQuitEvent
import org.bukkit.plugin.Plugin

class PlayerPotionListener(private val plugin: Plugin, private val potionList: PotionList) : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        // プレイヤーが参加したときにPotionListのインスタンスを作成
        potionList.addPotion(event.player, 0, 0) // 初期化のためのダミーエントリ
    }

//    @EventHandler
//    fun onPlayerQuit(event: PlayerQuitEvent) {
//        // プレイヤーが抜けたときにPotionListのインスタンスを削除
//        potionList.removePlayer(event.player)
//    }
}