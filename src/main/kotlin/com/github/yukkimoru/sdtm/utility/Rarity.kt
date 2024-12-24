package com.github.yukkimoru.sdtm.utility

import java.util.*

object RarityUtil {
	data class RarityInfo(val name: String, val section: String)

	fun getInfo(rarity: String): RarityInfo {
		return when (rarity.lowercase(Locale.getDefault())) {
			"common" -> RarityInfo("§f§lコモン", "§f")
			"uncommon" -> RarityInfo("§a§lアンコモン", "§a")
			"rare" -> RarityInfo("§9§lレア", "§9")
			"epic" -> RarityInfo("§5§lエピック", "§5")
			"legendary" -> RarityInfo("§6§lレジェンド", "§6")
			"mythic" -> RarityInfo("§d§lミシック", "§d")
			else -> RarityInfo("§4§k**§r§4§lNULL§r§4§k**", "§4")
		}
	}
}