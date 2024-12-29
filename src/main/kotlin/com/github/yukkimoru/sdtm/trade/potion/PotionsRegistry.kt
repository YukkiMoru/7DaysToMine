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

	private val healingPotions = (1..4).associate { level ->
		"healing$level" to PotionData(
			"§c治癒",
			"healing",
			level,
			listOf(PotionEffect(PotionEffectType.INSTANT_HEALTH, 200 * level, level)),
			"common",
			10,
			Color.RED,
			mapOf(Material.EMERALD to 1 * level)
		)
	}

	private val strengthPotions = (1..4).associate { level ->
		"strength$level" to PotionData(
			"§6力",
			"strength",
			level,
			listOf(PotionEffect(PotionEffectType.STRENGTH, 200 * level, level)),
			"common",
			10,
			Color.ORANGE,
			mapOf(Material.EMERALD to 1 * level)
		)
	}

	private val speedPotions = (1..4).associate { level ->
		"speed$level" to PotionData(
			"§b俊敏",
			"speed",
			level,
			listOf(PotionEffect(PotionEffectType.SPEED, 200 * level, level)),
			"common",
			10,
			Color.BLUE,
			mapOf(Material.EMERALD to 1 * level)
		)
	}

	private val giantPotion = (1..4).associate { level ->
		"giant$level" to PotionData(
			"§a巨人",
			"giant",
			level,
			listOf(PotionEffect(PotionEffectType.SLOWNESS, 200 * level, level)),
			"rare",
			10,
			Color.GREEN,
			mapOf(Material.EMERALD to 1 * level)
		)
	}

	private val midgetPotion = (1..4).associate { level ->
		"midget$level" to PotionData(
			"§7小人",
			"midget",
			level,
			listOf(PotionEffect(PotionEffectType.SLOWNESS, 200 * level, level)),
			"rare",
			10,
			Color.GRAY,
			mapOf(Material.EMERALD to 1 * level)
		)
	}

	val potions = healingPotions + strengthPotions + speedPotions + giantPotion + midgetPotion
}
