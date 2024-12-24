package com.github.yukkimoru.sdtm.trade.potion

import org.bukkit.Color
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.PotionMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

class PotionFactory(private val plugin: JavaPlugin) {
	data class PotionData(
		val potionName: String,
		val potionLevel: Int,
		val effects: List<PotionEffect> = listOf(),
		val rarity: String = "NULL",
		val duration: Int,
		val color: Color? = null,
		val potionCosts: Map<Material, Int> = mapOf(Material.EMERALD to 1)
	)

	val potions = mapOf(
		"healing" to PotionData(
			"§c治癒",
			1,
			listOf(PotionEffect(PotionEffectType.INSTANT_HEALTH, 200, 1)),
			"common",
			10,
			Color.RED,
			mapOf(Material.EMERALD to 1)
		),
		"strength" to PotionData(
			"§6力",
			1,
			listOf(PotionEffect(PotionEffectType.STRENGTH, 200, 1)),
			"rare",
			10,
			Color.ORANGE,
			mapOf(Material.EMERALD to 1)
		),
		"speed" to PotionData(
			"§b俊敏",
			1,
			listOf(PotionEffect(PotionEffectType.SPEED, 200, 1)),
			"uncommon",
			10,
			Color.BLUE,
			mapOf(Material.EMERALD to 1)
		),
		"giant" to PotionData(
			"§a巨人",
			1,
			listOf(PotionEffect(PotionEffectType.SLOWNESS, 200, 1)),
			"rare",
			10,
			Color.GREEN,
			mapOf(Material.EMERALD to 1)
		),
		"midget" to PotionData(
			"§e小人",
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
		displayMode: Boolean,
	): ItemStack {
		val itemStack = ItemStack(Material.POTION)
		val meta = itemStack.itemMeta as PotionMeta
		meta.setDisplayName("${potionName}のポーション")

		val container = meta.persistentDataContainer
		val potionNameKey = NamespacedKey(plugin, "potion_name")
		container.set(potionNameKey, PersistentDataType.STRING, potionName)
		val potionLevelKey = NamespacedKey(plugin, "potion_level")
		container.set(potionLevelKey, PersistentDataType.INTEGER, potionLevel)

		// Add effects to the potion
		val potionData = potions[potionName]
		potionData?.effects?.forEach { effect ->
			meta.addCustomEffect(effect, true)
		}
		itemStack.itemMeta = meta
		return itemStack
	}
}