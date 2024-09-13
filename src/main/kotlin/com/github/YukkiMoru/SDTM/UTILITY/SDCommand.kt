package com.github.YukkiMoru.SDTM.UTILITY

import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
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
		if (sender !is Player || args.isEmpty()) return false

		val player = sender
		when (args[0]) {
			"kill" -> executeCommand("kill @e[type=!player]", "kill が実行されました!")
			"help" -> sendHelpMessage(player)
			"debug" -> player.sendMessage("Debug mode set to: ${args.getOrNull(1)}")
			else -> player.sendMessage("不明なコマンドです: ${args[0]}, 詳しくは/sd helpを入力してください")
		}
		return true
	}

	private fun executeCommand(command: String, message: String) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
		Bukkit.broadcastMessage(message)
	}

	private fun sendHelpMessage(player: Player) {
		player.sendMessage("Usage: /sd <command> <args>")
		player.sendMessage("Commands: ")
		COMMANDS.forEach { (key, value) ->
			player.sendMessage("$key : ${value.joinToString(", ")}")
		}
	}

	override fun onTabComplete(
		sender: CommandSender,
		command: Command,
		alias: String,
		args: Array<String>
	): List<String>? {
		return if (sender is Player) {
			if (args.size == 1) COMMANDS.keys.toList() else COMMANDS[args[0]] ?: emptyList()
		} else null
	}

	companion object {
		private val COMMANDS = mapOf(
			"" to listOf("kill", "help", "debug"),
			"debug" to listOf("true", "false")
		)
	}

	fun registerCommands() {
		plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
			val commands = event.registrar()
			val commandNode: LiteralCommandNode<CommandSourceStack> =
				LiteralArgumentBuilder.literal<CommandSourceStack>("sd")
					.then(LiteralArgumentBuilder.literal<CommandSourceStack>("kill")
						.executes { ctx: CommandContext<CommandSourceStack> ->
							executeCommand("kill @e[type=!player]", "kill が実行されました!")
							com.mojang.brigadier.Command.SINGLE_SUCCESS
						}
					)
					.then(LiteralArgumentBuilder.literal<CommandSourceStack>("help")
						.executes { ctx: CommandContext<CommandSourceStack> ->
							ctx.source.sender.sendMessage(Component.text("Commands:", NamedTextColor.AQUA))
							COMMANDS.forEach { (key, value) ->
								ctx.source.sender.sendMessage(
									Component.text(
										"$key : ${value.joinToString(", ")}",
										NamedTextColor.AQUA
									)
								)
							}
							com.mojang.brigadier.Command.SINGLE_SUCCESS
						}
					)
					.then(LiteralArgumentBuilder.literal<CommandSourceStack>("debug")
						.then(LiteralArgumentBuilder.literal<CommandSourceStack>("true")
							.executes { ctx: CommandContext<CommandSourceStack> ->
								ctx.source.sender.sendMessage(
									Component.text(
										"Debug mode set to: true",
										NamedTextColor.AQUA
									)
								)
								com.mojang.brigadier.Command.SINGLE_SUCCESS
							}
						)
						.then(LiteralArgumentBuilder.literal<CommandSourceStack>("false")
							.executes { ctx: CommandContext<CommandSourceStack> ->
								ctx.source.sender.sendMessage(
									Component.text(
										"Debug mode set to: false",
										NamedTextColor.AQUA
									)
								)
								com.mojang.brigadier.Command.SINGLE_SUCCESS
							}
						)
					)
					.executes { ctx: CommandContext<CommandSourceStack> ->
						ctx.source.sender.sendMessage(Component.text("[SDTM]Hello World!", NamedTextColor.AQUA))
						com.mojang.brigadier.Command.SINGLE_SUCCESS
					}
					.build()
			commands.register(commandNode, "A command for 7DaysToMine plugin")
		}
	}
}