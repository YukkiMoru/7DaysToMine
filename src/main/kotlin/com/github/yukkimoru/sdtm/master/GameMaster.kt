package com.github.yukkimoru.sdtm.master

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import org.bukkit.event.Listener
import org.bukkit.plugin.java.JavaPlugin

class GameMaster(private val plugin: JavaPlugin) : Listener {
	fun startGameDay(day: Int) {
		when (day) {
			0 -> {
				// ゲーム開始までのカウントダウン(10秒)プレイヤーにメッセージを送信
				Bukkit.getScheduler().runTaskLater(plugin, Runnable {
					Bukkit.getOnlinePlayers().forEach { player: Player ->
						player.sendMessage(
							Component.text(
								"[SDTM] ゲーム開始まで10秒",
								NamedTextColor.AQUA
							)
						)
					}
				}, 1L)
				Bukkit.getScheduler().runTaskLater(plugin, Runnable {
					Bukkit.getOnlinePlayers().forEach { player: Player ->
						player.sendMessage(
							Component.text(
								"[SDTM] ゲーム開始まで5秒!",
								NamedTextColor.AQUA
							)
						)
					}
				}, 100L)
				Bukkit.getScheduler().runTaskLater(plugin, Runnable {
					Bukkit.getOnlinePlayers().forEach { player: Player ->
						player.sendMessage(
							Component.text(
								"[SDTM] ゲーム開始まで4秒!",
								NamedTextColor.AQUA
							)
						)
					}
				}, 120L)
				Bukkit.getScheduler().runTaskLater(plugin, Runnable {
					Bukkit.getOnlinePlayers().forEach { player: Player ->
						player.sendMessage(
							Component.text(
								"[SDTM] ゲーム開始まで3秒!",
								NamedTextColor.AQUA
							)
						)
					}
				}, 140L)
				Bukkit.getScheduler().runTaskLater(plugin, Runnable {
					Bukkit.getOnlinePlayers().forEach { player: Player ->
						player.sendMessage(
							Component.text(
								"[SDTM] ゲーム開始まで2秒!",
								NamedTextColor.AQUA
							)
						)
					}
				}, 160L)
				Bukkit.getScheduler().runTaskLater(plugin, Runnable {
					Bukkit.getOnlinePlayers().forEach { player: Player ->
						player.sendMessage(
							Component.text(
								"[SDTM] ゲーム開始まで1秒!",
								NamedTextColor.AQUA
							)
						)
					}
				}, 180L)
				Bukkit.getScheduler().runTaskLater(plugin, Runnable {
					Bukkit.getOnlinePlayers().forEach { player: Player ->
						player.sendMessage(
							Component.text(
								"[SDTM] ゲーム開始!",
								NamedTextColor.AQUA
							)
						)
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