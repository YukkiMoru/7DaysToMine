package com.github.yukkimoru.sdtm.utility

object Translate {

	val dictJP: Map<String, String> = mapOf(
		"DIAMOND" to "§b§lダイヤモンド",
		"EMERALD" to "§a§lエメラルド"
	)

	fun transEN2JP(text: String): String {
		return dictJP[text] ?: text
	}
}