package com.github.yukkimoru.sdtm.utility

import org.bukkit.Bukkit

class TabList {
	private fun setTabListHeaderAndFooter(header: String, footer: String) {
		for (player in Bukkit.getOnlinePlayers()) {
			player.setPlayerListHeaderFooter(header, footer)
		}
	}

	fun initialize() {
		setTabListHeaderAndFooter("§bWelcome to the §c7§aDays§9To§eMine§b!", "§bEnjoy your stay!")
	}
}