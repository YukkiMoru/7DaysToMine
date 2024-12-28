package com.github.yukkimoru.sdtm.trade.potion

import com.github.yukkimoru.sdtm.trade.potion.PotionsRegistry.potions
import com.github.yukkimoru.sdtm.trade.potion.PotionsRegistry.transPotionSize
import com.github.yukkimoru.sdtm.utility.ItemFactory
import com.github.yukkimoru.sdtm.utility.RarityUtil
import com.github.yukkimoru.sdtm.utility.Translate
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.Plugin

class PotionFactory(private val plugin: Plugin) {
	private val itemFactory = ItemFactory(plugin)

	fun createPotion(
		potionName: String,
		potionLevel: Int,
		displayMode: Boolean
	): ItemStack {
		val potionData =
			PotionsRegistry.potions.values.find { it.potionName == potionName && it.potionLevel == potionLevel }
				?: throw IllegalArgumentException("Invalid potionName:$potionName or potionLevel$potionLevel")

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

		val itemStack = itemFactory.createItemStack(
			Material.POTION,
			1,
			"§a${transPotionSize(potionLevel)}${potionData.potionDisplayName}ポーション",
			lore,
			potionData.rarity,
			customModelID = potionData.potionLevel
		)

		val meta = itemStack.itemMeta as PotionMeta
		val container = meta.persistentDataContainer
		val customModelIDKey = NamespacedKey(plugin, "potion_name")
		container.set(customModelIDKey, PersistentDataType.STRING, potionData.potionName)
		val potionLevelKey = NamespacedKey(plugin, "potion_level")
		container.set(potionLevelKey, PersistentDataType.INTEGER, potionData.potionLevel)

		potionData.color?.let { meta.color = it }
		potionData.effects.forEach { effect ->
			meta.addCustomEffect(effect, true)
		}

		itemStack.itemMeta = meta
		return itemStack
	}

	fun getPotionInfo(potionName: String, potionLevel: Int): PotionsRegistry.PotionData? {
		return potions.values.find { it.potionName == potionName && it.potionLevel == potionLevel }
	}
}