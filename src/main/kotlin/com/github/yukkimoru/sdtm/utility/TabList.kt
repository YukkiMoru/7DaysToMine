// src/main/kotlin/com/github/yukkimoru/sdtm/utility/TabList.kt
package com.github.yukkimoru.sdtm.utility

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.entity.Player

class TabList {

	fun initialize() {
		for (player in Bukkit.getOnlinePlayers()) {
			updatePotionCooldowns(player, emptyMap())
		}
	}

	fun updatePotionCooldowns(player: Player, cooldowns: Map<Int, Int>) {
		val header = Component.text("Potion Cooldowns", NamedTextColor.GOLD, TextDecoration.BOLD)
		val footer = Component.text(buildString {
			append("Cooldowns:\n")
			for ((potionID, timeLeft) in cooldowns) {
				append("Potion $potionID: $timeLeft seconds\n")
			}
		}, NamedTextColor.WHITE)

		player.sendPlayerListHeaderAndFooter(header, footer)
	}
}