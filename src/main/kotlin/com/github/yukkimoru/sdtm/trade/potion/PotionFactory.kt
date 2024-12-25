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
		val potionName: String,
		val customModelID: Int,
		val effects: List<PotionEffect> = listOf(),
		val rarity: String = "NULL",
		val duration: Int,
		val color: Color? = null,
		val potionCosts: Map<Material, Int> = mapOf(Material.EMERALD to 1)
	)

	val potions = mapOf(
		"healing" to PotionData(
			"§c治癒",
			1000,
			listOf(PotionEffect(PotionEffectType.INSTANT_HEALTH, 200, 1)),
			"common",
			10,
			Color.RED,
			mapOf(Material.EMERALD to 1)
		),
		"strength" to PotionData(
			"§6力",
			1010,
			listOf(PotionEffect(PotionEffectType.STRENGTH, 200, 1)),
			"rare",
			10,
			Color.ORANGE,
			mapOf(Material.EMERALD to 1)
		),
		"speed" to PotionData(
			"§b俊敏",
			1020,
			listOf(PotionEffect(PotionEffectType.SPEED, 200, 1)),
			"uncommon",
			10,
			Color.BLUE,
			mapOf(Material.EMERALD to 1)
		),
		"giant" to PotionData(
			"§a巨人",
			1030,
			listOf(PotionEffect(PotionEffectType.SLOWNESS, 200, 1)),
			"rare",
			10,
			Color.GREEN,
			mapOf(Material.EMERALD to 1)
		),
		"midget" to PotionData(
			"§e小人",
			1040,
			listOf(PotionEffect(PotionEffectType.SLOWNESS, 200, 1)),
			"rare",
			10,
			Color.YELLOW,
			mapOf(Material.EMERALD to 1)
		)
	)

	fun createPotion(
		customModelID: Int,
		displayMode: Boolean,
	): ItemStack {
		// get potion name from the map using the potion

		val potionData = potions.values.find { it.customModelID == customModelID }
		if (potionData == null) {
			plugin.logger.warning("PotionData not found for customModelID: $customModelID")
			return ItemStack(Material.AIR)
		}

		val itemStack = ItemStack(Material.POTION)
		val meta = itemStack.itemMeta as PotionMeta
		meta.setDisplayName("${potionData.potionName}のポーション")

		val container = meta.persistentDataContainer
		val potionLevelKey = NamespacedKey(plugin, "custom_model_id")
		container.set(potionLevelKey, PersistentDataType.INTEGER, potionData.customModelID)

		// Add effects to the potion
		potionData.color?.let { meta.color = it }
		potionData.effects.forEach { effect ->
			meta.addCustomEffect(effect, true)
		}
		val rarity = potionData.rarity
		meta.lore = mutableListOf<String>().apply {
			if (displayMode) {
				add("§a必要素材:")
				addAll(potionData.potionCosts.entries.map { "§a${Translate.transEN2JP(it.key.name)} x${it.value}" })
			}
			add(RarityUtil.getInfo(rarity).name)
		}

		itemStack.itemMeta = meta
		return itemStack
	}

	fun getPotionInfo(customModelID: Int): PotionData? {
		return potions.values.find { it.customModelID == customModelID }
	}
}