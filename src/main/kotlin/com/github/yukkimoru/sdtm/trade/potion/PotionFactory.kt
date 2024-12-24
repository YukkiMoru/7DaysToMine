package com.github.yukkimoru.sdtm.trade.potion

import com.github.yukkimoru.sdtm.utility.RarityUtil
import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType
import kotlin.text.get

class PotionFactory(private val plugin: JavaPlugin) {
	data class PotionData(
		val potionName: String,
		val rarity: String = "NULL",
		val duration: Int,
		val potionCosts: Map<Material, Int> = mapOf(Material.EMERALD to 1)
	)

	private val potions = mapOf(
		"healing" to PotionData(
			"§c治癒",
			"common",
			10,
			mapOf(Material.EMERALD to 1)
		),
		"strength" to PotionData(
			"§6力",
			"rare",
			10,
			mapOf(Material.DIAMOND to 1)
		),
		"speed" to PotionData(
			"§b俊敏",
			"uncommon",
			10,
			mapOf(Material.GOLD_INGOT to 1)
		),
		"giant" to PotionData(
			"§a巨人",
			"rare",
			10,
			mapOf(Material.IRON_INGOT to 1)
		),
		"midget" to PotionData(
			"§e小人",
			"rare",
			10,
			mapOf(Material.NETHERITE_INGOT to 1)
		)
	)

	private fun createPotion(
		potionDisplayName: String,
		potionLevel: Int,
		duration: Int,
		enableLore: Boolean? = false,
		lore: List<String>,
		rarity: String,
		customModelData: Int? = null,
		color: Color? = null,
		effects: List<PotionEffect> = listOf()
	): ItemStack {
		val itemStack = ItemStack(Material.POTION)
		val meta = itemStack.itemMeta as PotionMeta
		meta.setDisplayName("$(potionDisplayName)のポーション")
		if (enableLore == true) {
			meta.lore = listOf(
				"§r${RarityUtil.getInfo(rarity).section}=============",
				lore.joinToString(),
				RarityUtil.getInfo(rarity).name,
				"§r${RarityUtil.getInfo(rarity).section}============="
			)
		} else {
			meta.lore = listOf(
//				"§r${RarityUtil.getInfo(rarity).section}=============",
				RarityUtil.getInfo(rarity).name,
//				"§r${RarityUtil.getInfo(rarity).section}============="
			)
		}

		val container = meta.persistentDataContainer
		val potionNameKey = NamespacedKey(plugin, "potion_name")
		container.set(potionNameKey, PersistentDataType.STRING, potionDisplayName)
		val potionLevelKey = NamespacedKey(plugin, "potion_level")
		container.set(potionLevelKey, PersistentDataType.INTEGER, potionLevel)
		val durationKey = NamespacedKey(plugin, "duration")
		container.set(durationKey, PersistentDataType.INTEGER, duration)
		customModelData?.let {
			meta.setCustomModelData(it)
		}
		color?.let {
			meta.color = it
		}
		effects.forEach { effect ->
			meta.addCustomEffect(effect, true)
		}
		itemStack.itemMeta = meta
		return itemStack
	}

	fun getPotion(potionName: String, potionLevel: Int, displayMode: Boolean): ItemStack {
		val potionData = potions[potionName] ?: throw IllegalArgumentException("Invalid potion name")
		val lore = if (displayMode) {
			listOf(
				"§fポーションの効果:",
				"§a必要素材:",
				*(potionData.potionCosts.entries.map { "§a${it.key.name} x${it.value}" }.toTypedArray())
			)
		} else {
			listOf("§fポーションの効果")
		}
		val (color, effects) = when (potionName to potionLevel) {
			"healing" to 1 -> Color.RED to listOf(PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 1))
			"strength" to 1 -> Color.ORANGE to listOf(PotionEffect(PotionEffectType.STRENGTH, 200, 1))
			"speed" to 1 -> Color.BLUE to listOf(PotionEffect(PotionEffectType.SPEED, 200, 1))
			"giant" to 1 -> Color.GREEN to listOf(PotionEffect(PotionEffectType.SLOWNESS, 200, 1))
			"midget" to 1 -> Color.YELLOW to listOf(PotionEffect(PotionEffectType.SLOWNESS, 200, 1))
			else -> null to listOf()
		}
		return createPotion(
			potionData.potionName,
			potionLevel,
			potionData.duration,
			displayMode,
			lore,
			potionData.rarity,
			customModelData = null,
			color = color,
			effects = effects
		)
	}
}