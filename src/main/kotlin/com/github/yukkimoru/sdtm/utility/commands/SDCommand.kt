package com.github.yukkimoru.sdtm.utility.commands

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player
import org.bukkit.plugin.java.JavaPlugin

class SDCommand(private val plugin: JavaPlugin) : CommandExecutor, TabCompleter {

	init {
		plugin.getCommand("sd")?.apply {
			setExecutor(this@SDCommand)
			tabCompleter = this@SDCommand
		}
	}

	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
		plugin.logger.info("SDCommand onCommand called with args: ${args.joinToString()}")

		synchronized(this) {
			if (args.isEmpty()) {
				sender.sendMessage(Component.text("[SDTM] Hello World!", NamedTextColor.AQUA))
				return true
			}

			when (args[0].lowercase()) {
				"kill" -> {
					executeCommand("kill @e[type=!minecraft:player,type=!minecraft:block_display,type=!minecraft:villager]")
					sender.sendMessage(
						Component.text(
							"[SDTM] プレイヤー以外のエンティティを全て削除しました",
							NamedTextColor.AQUA
						)
					)
				}

				"debug" -> {
					if (args.size > 1) {
						when (args[1].lowercase()) {
							"true" -> sender.sendMessage(
								Component.text(
									"[SDTM] デバッグモードを有効にしました",
									NamedTextColor.AQUA
								)
							)

							"false" -> sender.sendMessage(
								Component.text(
									"[SDTM] デバッグモードを無効にしました",
									NamedTextColor.AQUA
								)
							)
						}
					}
				}

				"horde" -> {
					if (args.size > 1 && args[1].lowercase() == "start" && args.size > 2) {
						val level = args[2].toIntOrNull()
						if (level != null) {
							startHorde(level)
							sender.sendMessage(
								Component.text(
									"[SDTM] ホード$level が開始されました",
									NamedTextColor.AQUA
								)
							)
						}
					}
				}

				"scan" -> {
					if (args.size > 3) {
						val x = args[1].toIntOrNull()
						val y = args[2].toIntOrNull()
						val z = args[3].toIntOrNull()
						if (x != null && y != null && z != null) {
							scanSpawnBlock(x, y, z)
							sender.sendMessage(
								Component.text(
									"[SDTM] スキャンを開始しました: ($x, $y, $z)",
									NamedTextColor.AQUA
								)
							)
						}
					}
				}
			}
		}
		return true
	}

	private fun executeCommand(command: String) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
	}

	private fun startHorde(level: Int) {
		val spawnMobs = SpawnMobs(plugin)
		spawnMobs.spawnHorde(level)
	}

	private fun scanSpawnBlock(x: Int, y: Int, z: Int) {
		val scanBlocks = ScanBlocks()
		scanBlocks.scan(plugin, x, y, z)
	}

	override fun onTabComplete(
		sender: CommandSender,
		command: Command,
		alias: String,
		args: Array<String>
	): List<String>? {
		return if (sender is Player) {
			when (args.size) {
				1 -> listOf("kill", "debug", "horde", "scan")
				2 -> when (args[0].lowercase()) {
					"debug" -> listOf("true", "false")
					"horde" -> listOf("start")
					"scan" -> listOf("100 60 100")
					else -> emptyList()
				}

				else -> emptyList()
			}
		} else null
	}
}