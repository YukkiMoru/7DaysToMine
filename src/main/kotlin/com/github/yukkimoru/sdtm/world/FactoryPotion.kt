package com.github.yukkimoru.sdtm.world

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionType

class FactoryPotion(private val plugin: JavaPlugin) {

	private fun createPotion(
		PotionID: Int?,
		name: String,
		potionType: PotionType,
		lore: List<String>,
		rarity: String,
		customModelData: Int? = null,
	): ItemStack {
		val itemStack = ItemStack(Material.POTION)
		val meta = itemStack.itemMeta as PotionMeta
		meta.setDisplayName(name)
		meta.lore = lore
		val container = meta.persistentDataContainer
		val key = NamespacedKey(plugin, "rarity")
		container.set(key, PersistentDataType.STRING, rarity)
		customModelData?.let {
			meta.setCustomModelData(it)
		}
		itemStack.itemMeta = meta
		return itemStack
	}

	fun createHealingPotion(): ItemStack {
		return createPotion(
			PotionID = 1,
			name = "§cHealing Potion",
			potionType = PotionType.HEALING,
			lore = listOf("§7A potion that heals instantly."),
			rarity = "common"
		)
	}

	fun createStrengthPotion(): ItemStack {
		return createPotion(
			PotionID = 2,
			name = "§6Strength Potion",
			potionType = PotionType.STRENGTH,
			lore = listOf("§7A potion that increases strength."),
			rarity = "rare",
			customModelData = 301
		)
	}

	fun createSpeedPotion(): ItemStack {
		return createPotion(
			PotionID = 3,
			name = "§bSpeed Potion",
			potionType = PotionType.LEAPING,
			lore = listOf("§7A potion that increases speed."),
			rarity = "uncommon",
			customModelData = 302
		)
	}
}