package com.github.yukkimoru.sdtm.utility

import com.github.yukkimoru.sdtm.trade.potion.PotionsRegistry
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

	fun updatePotionCooldowns(player: Player, cooldowns: Map<String, Int>) {
		val header = Component.text("Potion Cooldowns", NamedTextColor.GOLD, TextDecoration.BOLD)
		val footer = Component.text(buildString {
			for ((potionNameLevel, timeLeft) in cooldowns) {
				if (isNotEmpty()) append("§r/")
				val potionData = PotionsRegistry.potions[potionNameLevel]
				append("${potionData?.potionDisplayName}${potionData?.potionLevel?.let { NumConv.intToRoman(it) }}:${timeLeft}s")
			}
		}, NamedTextColor.WHITE)

		player.sendPlayerListHeaderAndFooter(header, footer)
	}
}