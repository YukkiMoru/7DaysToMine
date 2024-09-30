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
		potionType: PotionType,
		name: String,
		lore: List<String>,
		rarity: String,
		customModelData: Int? = null
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
			PotionType.HEALING,
			"§cHealing Potion",
			listOf("§7A potion that heals instantly."),
			"common",
			300
		)
	}

	fun createStrengthPotion(): ItemStack {
		return createPotion(
			PotionType.STRENGTH,
			"§6Strength Potion",
			listOf("§7A potion that increases strength."),
			"rare",
			301
		)
	}

	fun createSpeedPotion(): ItemStack {
		return createPotion(
			PotionType.STRONG_LEAPING,
			"§bSpeed Potion",
			listOf("§7A potion that increases speed."),
			"uncommon",
			302
		)
	}
}