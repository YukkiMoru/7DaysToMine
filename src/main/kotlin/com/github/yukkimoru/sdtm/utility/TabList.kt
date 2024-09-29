package com.github.yukkimoru.sdtm.utility

import org.bukkit.Bukkit

class TabList {

	fun setTabListHeaderAndFooter(header: String, footer: String) {
		for (player in Bukkit.getOnlinePlayers()) {
			player.setPlayerListHeaderFooter(header, footer)
		}
	}
}