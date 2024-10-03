package com.github.yukkimoru.sdtm.utility

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class TabList {

	fun initialize() {
		setTabListHeaderAndFooter(
			Component.text("Welcome to the 7DaysToMine!", NamedTextColor.AQUA),
			Component.text("Enjoy your stay!", NamedTextColor.AQUA)
		)
	}

	private fun setTabListHeaderAndFooter(header: Component, footer: Component) {
		for (player in Bukkit.getOnlinePlayers()) {
			player.sendPlayerListHeader(header)
			player.sendPlayerListFooter(footer)
		}
	}

	fun updatePotionCooldowns(player: Player, cooldowns: Map<Int, Int>) {
		val cooldownText = Component.text("Cooldowns: ", NamedTextColor.AQUA)
			.decoration(TextDecoration.BOLD, true)
			.append(
				Component.text(
					cooldowns.entries.joinToString(" ") { (potionID, timeLeft) ->
						"P$potionID: $timeLeft"
					},
					NamedTextColor.GRAY
				)
			)

		val playerName = Component.text(player.name, NamedTextColor.WHITE)
		val newPlayerListName = playerName.append(Component.text(" ")).append(cooldownText)

		player.playerListName(newPlayerListName)
	}
}