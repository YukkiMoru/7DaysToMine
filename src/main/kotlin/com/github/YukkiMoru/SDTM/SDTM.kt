package com.github.YukkiMoru.SDTM

import org.bukkit.plugin.java.JavaPlugin
import com.mojang.brigadier.Command
import com.mojang.brigadier.context.CommandContext
import com.mojang.brigadier.arguments.StringArgumentType
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import com.mojang.brigadier.builder.RequiredArgumentBuilder
import com.mojang.brigadier.tree.LiteralCommandNode
import io.papermc.paper.command.brigadier.CommandSourceStack
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class SDTM : JavaPlugin() {

    override fun onEnable() {
        logger.info("SDTM plugin enabled")

        // Register command using LifecycleEventManager
        val manager = this.lifecycleManager
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

        // Register the OreRegeneration class
        server.pluginManager.registerEvents(OreRegeneration(this), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}