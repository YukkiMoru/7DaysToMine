package com.github.YukkiMoru.SDTM

import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor

class SDCommand(private val plugin: SDTM) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
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
}