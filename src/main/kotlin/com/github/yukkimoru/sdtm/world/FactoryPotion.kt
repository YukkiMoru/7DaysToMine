package com.github.yukkimoru.sdtm.world

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class FactoryPotion(private val plugin: JavaPlugin) {

	private fun createPotion(
		potionID: Int?,
		name: String,
		duration: Int,
		lore: List<String>,
		rarity: String,
		customModelData: Int? = null,
		color: Color? = null,
		effects: List<PotionEffect> = emptyList()
	): ItemStack {
		val itemStack = ItemStack(Material.POTION)
		val meta = itemStack.itemMeta as PotionMeta
		meta.setDisplayName(name)
		meta.lore = lore
		val container = meta.persistentDataContainer
		val rarityKey = NamespacedKey(plugin, "rarity")
		container.set(rarityKey, PersistentDataType.STRING, rarity)
		potionID?.let {
			val potionIDKey = NamespacedKey(plugin, "PotionID")
			container.set(potionIDKey, PersistentDataType.INTEGER, it)
		}
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

	fun createHealingPotion(): ItemStack {
		return createPotion(
			potionID = 1,
			name = "§cHealing Potion",
			duration = 10,
			lore = listOf(),
			rarity = "common",
			color = Color.RED,
			effects = listOf(PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 1),
				PotionEffect(PotionEffectType.BLINDNESS, 200, 2))
		)
	}

	fun createStrengthPotion(): ItemStack {
		return createPotion(
			potionID = 2,
			name = "§6Strength Potion",
			duration = 10,
			lore = listOf("§7A potion that increases strength."),
			rarity = "rare",
			customModelData = 301,
			color = Color.ORANGE,
			effects = listOf(PotionEffect(PotionEffectType.STRENGTH, 200, 1))
		)
	}

	fun createSpeedPotion(): ItemStack {
		return createPotion(
			potionID = 3,
			name = "§bSpeed Potion",
			duration = 10,
			lore = listOf("§7A potion that increases speed."),
			rarity = "uncommon",
			customModelData = 302,
			color = Color.BLUE,
			effects = listOf(PotionEffect(PotionEffectType.SPEED, 200, 1))
		)
	}

	fun createGiantPotion(): ItemStack {
		return createPotion(
			potionID = 4,
			name = "§aGiant Potion",
			duration = 10,
			lore = listOf("§7A potion that makes you giant."),
			rarity = "rare",
			customModelData = 303,
			color = Color.GREEN,
		)
	}

	fun createMidgetPotion(): ItemStack {
		return createPotion(
			potionID = 5,
			name = "§eMidget Potion",
			duration = 10,
			lore = listOf("§7A potion that makes you midget."),
			rarity = "rare",
			customModelData = 304,
			color = Color.YELLOW,
		)
	}
}