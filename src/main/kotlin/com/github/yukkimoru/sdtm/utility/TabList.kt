package com.github.yukkimoru.sdtm.utility

import org.bukkit.Bukkit
import org.bukkit.entity.Player

class TabList {

	fun initialize() {
		setTabListHeaderAndFooter("§bWelcome to the §c7§aDays§9To§eMine§b!", "§bEnjoy your stay!")
	}

	private fun setTabListHeaderAndFooter(header: String, footer: String) {
		for (player in Bukkit.getOnlinePlayers()) {
			player.setPlayerListHeaderFooter(header, footer)
		}
	}

	fun updatePotionCooldowns(player: Player, cooldowns: Map<Int, Int>) {
		val cooldownText = buildString {
			append("§bCooldowns: ")
			for ((potionID, timeLeft) in cooldowns) {
				append("§7P$potionID: §c$timeLeft ")
			}
		}
		player.setPlayerListName("${player.name} $cooldownText")
	}
}