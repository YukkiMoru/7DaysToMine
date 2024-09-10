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

        // Register command using SDCommand class
        val sdCommand = SDCommand(this)
        sdCommand.registerCommands()

        // Register the OreRegeneration class
        server.pluginManager.registerEvents(OreRegeneration(this), this)
    }

    override fun onDisable() {
        // Plugin shutdown logic
    }
}