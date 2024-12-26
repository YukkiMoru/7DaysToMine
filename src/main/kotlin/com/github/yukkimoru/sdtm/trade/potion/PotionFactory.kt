package com.github.yukkimoru.sdtm.trade.potion

import com.github.yukkimoru.sdtm.utility.RarityUtil
import com.github.yukkimoru.sdtm.utility.Translate
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PotionFactory(private val plugin: Plugin) {
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

	private fun transPotionSize(size: Int): String {
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

	fun createPotion(
		potionName: String,
		potionLevel: Int,
		displayMode: Boolean
	): ItemStack {
		val potionData = potions.values.find { it.potionName == potionName && it.potionLevel == potionLevel }
			?: throw IllegalArgumentException("Invalid potionName:$potionName or potionLevel$potionLevel")

		val itemStack = ItemStack(Material.POTION)
		val meta = itemStack.itemMeta as PotionMeta

		meta.setDisplayName("§a${transPotionSize(potionLevel)}${potionData.potionDisplayName}ポーション")

		val container = meta.persistentDataContainer
		val potionLevelKey = NamespacedKey(plugin, "custom_model_id")
		container.set(potionLevelKey, PersistentDataType.INTEGER, potionData.potionLevel)

		potionData.color?.let { meta.color = it }
		potionData.effects.forEach { effect ->
			meta.addCustomEffect(effect, true)
		}

		val lore = if (displayMode) {
			mutableListOf<String>().apply {
				add("§r§5§l===必要素材===")
				addAll(potionData.potionCosts.entries.map { "§a${Translate.transEN2JP(it.key.name)} x${it.value}" })
				add("§r§5§l============")
				add(RarityUtil.getInfo(potionData.rarity).name)
			}
		} else {
			mutableListOf(RarityUtil.getInfo(potionData.rarity).name)
		}

		meta.lore = lore
		itemStack.itemMeta = meta
		return itemStack
	}

	fun getPotionInfo(potionName: String, potionLevel: Int): PotionData? {
		return potions.values.find { it.potionName == potionName && it.potionLevel == potionLevel }
	}
}