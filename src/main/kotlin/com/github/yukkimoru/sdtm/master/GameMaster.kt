package com.github.yukkimoru.sdtm.master

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class GameMaster(private val plugin: JavaPlugin) : Listener {
	fun startGameDay(day: Int) {
		when (day) {
			0 -> {
				// ゲーム開始までのカウントダウン(10秒)プレイヤーにメッセージを送信
				val world = Bukkit.getWorld("world")
				val entity = Bukkit.getOnlinePlayers()
					.firstOrNull()
				val countdownMessages = listOf(
					Pair("[SDTM] ゲーム開始まで10秒", 1L),
					Pair("[SDTM] ゲーム開始まで5秒!", 100L),
					Pair("[SDTM] ゲーム開始まで4秒!", 120L),
					Pair("[SDTM] ゲーム開始まで3秒!", 140L),
					Pair("[SDTM] ゲーム開始まで2秒!", 160L),
					Pair("[SDTM] ゲーム開始まで1秒!", 180L),
					Pair("[SDTM] ゲーム開始!", 200L)
				)
				countdownMessages.forEach { (message, delay) ->
					Bukkit.getScheduler().runTaskLater(plugin, Runnable {
						Bukkit.getOnlinePlayers().forEach { player: Player ->
							player.sendMessage(
								Component.text(message, NamedTextColor.AQUA)
							)
							world?.playSound(player.location, "minecraft:block.note_block.hat", 1.2f, 0.1f)
						}
					}, delay)
				}
				Bukkit.getScheduler().runTaskLater(plugin, Runnable {
					entity?.let {
						// worldにいるプレイヤー全員を特定の位置にテレポート
						val targetLocation = Location(world, 106.0, 10.0, 100.0) // x, y, z にテレポート先の座標を指定
						world?.players?.forEach { player ->
							player.teleport(targetLocation)
						}
						Bukkit.getScheduler().runTaskLater(plugin, Runnable {
							entity.let {
								world?.playSound(it.location, "minecraft:ambient.basalt_deltas.mood", 1.0f, 0.1f)
							}
						}, 1L)
					}
				}, 200L)
			}

			1 -> {
				// 1日目の処理
				Bukkit.getOnlinePlayers().forEach { player: Player ->
					player.sendMessage(
						Component.text(
							"[SDTM] 1日目が始まりました",
							NamedTextColor.AQUA
						)
					)
				}
			}
		}
	}
}