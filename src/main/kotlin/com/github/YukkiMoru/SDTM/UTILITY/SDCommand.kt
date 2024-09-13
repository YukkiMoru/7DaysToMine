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

	fun registerCommands() {
		plugin.lifecycleManager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
			val commands = event.registrar()
			val commandNode: LiteralCommandNode<CommandSourceStack> =
				LiteralArgumentBuilder.literal<CommandSourceStack>("sd")
					.then(LiteralArgumentBuilder.literal<CommandSourceStack>("kill")
						.executes { ctx: CommandContext<CommandSourceStack> ->
							executeCommand("kill @e[type=!player]")
							ctx.source.sender.sendMessage(
								Component.text(
									"[SDTM]プレイヤー以外のエンティティを全て削除しました",
									NamedTextColor.AQUA
								)
							)
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

	override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
		return true
	}

	private fun executeCommand(command: String) {
		Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command)
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

	// tabの補完候補機能の定義
	companion object {
		private val COMMANDS = mapOf(
			"" to listOf("kill", "debug"),
			"debug" to listOf("true", "false")
		)
	}
}