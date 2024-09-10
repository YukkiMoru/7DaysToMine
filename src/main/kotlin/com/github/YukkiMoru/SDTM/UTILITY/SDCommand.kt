package com.github.YukkiMoru.SDTM.UTILITY

import com.github.YukkiMoru.SDTM.SDTM
import com.mojang.brigadier.Command
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SDCommand(private val plugin: SDTM) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: org.bukkit.command.Command, label: String, args: Array<String>): Boolean {
        if (sender is Player) {
            val player = sender
            if (args.isNotEmpty()) {
                val position = args[0]
                // Nexus position setting logic here
                player.sendMessage(Component.text("Nexus position set to: $position", NamedTextColor.AQUA))
                return true
            } else {
                player.sendMessage(Component.text("Hello World!", NamedTextColor.AQUA))
                return true
            }
        } else {
            sender.sendMessage("This command can only be run by a player.")
            return false
        }
    }

    fun registerCommands() {
        val manager = plugin.lifecycleManager
        manager.registerEventHandler(LifecycleEvents.COMMANDS) { event ->
            val commands = event.registrar()
            val commandNode: LiteralCommandNode<CommandSourceStack> = LiteralArgumentBuilder.literal<CommandSourceStack>("sd")
                .then(
                    RequiredArgumentBuilder.argument<CommandSourceStack, String>("position", StringArgumentType.string())
                        .executes { ctx: CommandContext<CommandSourceStack> ->
                            val position = StringArgumentType.getString(ctx, "position")
                            ctx.source.sender.sendMessage(Component.text("Nexus position set to: $position", NamedTextColor.AQUA))
                            Command.SINGLE_SUCCESS
                        }
                )
                .executes { ctx: CommandContext<CommandSourceStack> ->
                    ctx.source.sender.sendMessage(Component.text("Hello World!", NamedTextColor.AQUA))
                    Command.SINGLE_SUCCESS
                }
                .build()

            commands.register(commandNode, "A command for 7DaysToMine plugin", listOf("sd-alias"))
        }
    }
}