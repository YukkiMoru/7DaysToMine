package com.github.yukkimoru.sdtm.trade.potion

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object PotionsRegistry {
	data class PotionData(
		val potionDisplayName: String,
		val potionName: String,
		val potionLevel: Int,
		val effects: List<PotionEffect> = listOf(),
		val rarity: String = "NULL",
		val duration: Int,
		val color: Color? = null,
		val potionCosts: Map<Material, Int> = mapOf(Material.EMERALD to 1)
	)

	private val potionSize: Map<Int, String> = mapOf(
		1 to "ちょびっとな",
		2 to "そこそこの", // デフォルトのポーションの形
		3 to "まぁまぁな",
		4 to "どっさりな"
	)

	fun transPotionSize(size: Int): String {
		return potionSize[size] ?: "大きさが不明な"
	}

	val potions = mapOf(
		"healing" to PotionData(
			"§c治癒",
			"healing",
			1,
			listOf(PotionEffect(PotionEffectType.INSTANT_HEALTH, 200, 1)),
			"common",
			10,
			Color.RED,
			mapOf(Material.EMERALD to 1)
		),
		"strength" to PotionData(
			"§6力",
			"strength",
			1,
			listOf(PotionEffect(PotionEffectType.STRENGTH, 200, 1)),
			"rare",
			10,
			Color.ORANGE,
			mapOf(Material.EMERALD to 1)
		),
		"speed" to PotionData(
			"§b俊敏",
			"speed",
			1,
			listOf(PotionEffect(PotionEffectType.SPEED, 200, 1)),
			"uncommon",
			10,
			Color.BLUE,
			mapOf(Material.EMERALD to 1)
		),
		"giant" to PotionData(
			"§a巨人",
			"giant",
			1,
			listOf(PotionEffect(PotionEffectType.SLOWNESS, 200, 1)),
			"rare",
			10,
			Color.GREEN,
			mapOf(Material.EMERALD to 1)
		),
		"midget" to PotionData(
			"§e小人",
			"midget",
			1,
			listOf(PotionEffect(PotionEffectType.SLOWNESS, 200, 1)),
			"rare",
			10,
			Color.YELLOW,
			mapOf(Material.EMERALD to 1)
		)
	)
}
