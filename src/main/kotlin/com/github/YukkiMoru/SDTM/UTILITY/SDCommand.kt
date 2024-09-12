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
		plugin.getCommand("sd")?.setExecutor(this)
		plugin.getCommand("sd")?.tabCompleter = this
	}

	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
		if (sender !is Player) {
			return false
		}

		val player = sender
		if (args.isEmpty()) {
			return false
		}

		when (args[0]) {
			"kill" -> executeDefaultCommand(args, "kill", "kill @e[type=!player]")
			"help" -> handleHelpCommand(player)
			"debug" -> handleDebugCommand(player, args)
			else -> player.sendMessage("不明なコマンドです: ${args[0]}, 詳しくは/sd helpを入力してください")
		}
		return true
	}

	private fun executeDefaultCommand(args: Array<String>, aliasCommand: String, executeCommand: String) {
		if (args[0] == aliasCommand) {
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), executeCommand)
			Bukkit.broadcastMessage("$aliasCommand が実行されました!")
		}
	}

	private fun handleHelpCommand(player: Player) {
		player.sendMessage("Usage: /sd <command> <args>")
		player.sendMessage("Commands: ")
		COMMANDS.forEach { (key, value) ->
			player.sendMessage("$key : ${value.joinToString(", ")}")
		}
	}

	private fun handleDebugCommand(player: Player, args: Array<String>) {
		if (args.size < 2) {
			player.sendMessage("Usage: /sd debug <true/false>")
			return
		}
		val debugMode = args[1].toBoolean()
		player.sendMessage("Debug mode set to: $debugMode")
	}

	override fun onTabComplete(
		sender: CommandSender,
		command: Command,
		alias: String,
		args: Array<String>
	): List<String>? {
		return if (sender is Player) {
			if (args.size == 1) {
				COMMANDS.keys.toList()
			} else {
				COMMANDS[args[0]] ?: emptyList()
			}
		} else {
			null
		}
	}

	companion object {
		private val COMMANDS = mapOf(
			"" to listOf("kill", "help", "debug", "gui", "config"),
			"debug" to listOf("true", "false"),
			"gui" to listOf("PlatformGUI", "TowerGUI"),
			"config" to listOf("show")
		)
	}

	fun registerCommands() {
		val manager = plugin.lifecycleManager
		manager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
			val commands = event.registrar()
			val commandNode: LiteralCommandNode<CommandSourceStack> =
				LiteralArgumentBuilder.literal<CommandSourceStack>("sd")
					.then(
						LiteralArgumentBuilder.literal<CommandSourceStack>("kill")
							.executes { ctx: CommandContext<CommandSourceStack> ->
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kill @e[type=!player]")
								Bukkit.broadcastMessage("kill が実行されました!")
								com.mojang.brigadier.Command.SINGLE_SUCCESS
							}
					)
					.then(
						LiteralArgumentBuilder.literal<CommandSourceStack>("help")
							.executes { ctx: CommandContext<CommandSourceStack> ->
								ctx.source.sender.sendMessage(
									Component.text("Usage: /sd <command> <args>", NamedTextColor.AQUA)
								)
								ctx.source.sender.sendMessage(Component.text("Commands: ", NamedTextColor.AQUA))
								COMMANDS.forEach { (key, value) ->
									ctx.source.sender.sendMessage(
										Component.text("$key : ${value.joinToString(", ")}", NamedTextColor.AQUA)
									)
								}
								com.mojang.brigadier.Command.SINGLE_SUCCESS
							}
					)
					.then(
						LiteralArgumentBuilder.literal<CommandSourceStack>("debug")
							.then(
								LiteralArgumentBuilder.literal<CommandSourceStack>("true")
									.executes { ctx: CommandContext<CommandSourceStack> ->
										ctx.source.sender.sendMessage(
											Component.text("Debug mode set to: true", NamedTextColor.AQUA)
										)
										com.mojang.brigadier.Command.SINGLE_SUCCESS
									}
							)
							.then(
								LiteralArgumentBuilder.literal<CommandSourceStack>("false")
									.executes { ctx: CommandContext<CommandSourceStack> ->
										ctx.source.sender.sendMessage(
											Component.text("Debug mode set to: false", NamedTextColor.AQUA)
										)
										com.mojang.brigadier.Command.SINGLE_SUCCESS
									}
							)
					)
					.executes { ctx: CommandContext<CommandSourceStack> ->
						ctx.source.sender.sendMessage(Component.text("Hello World!", NamedTextColor.AQUA))
						com.mojang.brigadier.Command.SINGLE_SUCCESS
					}
					.build()
			commands.register(commandNode, "A command for 7DaysToMine plugin")
		}
	}
}