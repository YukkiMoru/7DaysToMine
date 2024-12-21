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
		enableLore: Boolean? = false,
		lore: List<String>,
		rarity: String,
		customModelData: Int? = null,
		color: Color? = null,
		effects: List<PotionEffect> = listOf()
	): ItemStack {
		val itemStack = ItemStack(Material.POTION)
		val meta = itemStack.itemMeta as PotionMeta
		meta.setDisplayName(name)
		if (enableLore == true) {
			meta.lore = listOf(
				"§r${RarityUtil.getInfo(rarity).section}=============",
				lore.joinToString(),
				RarityUtil.getInfo(rarity).name,
				"§r${RarityUtil.getInfo(rarity).section}============="
			)
		} else {
			meta.lore = listOf(
				"§r${RarityUtil.getInfo(rarity).section}=============",
				RarityUtil.getInfo(rarity).name,
				"§r${RarityUtil.getInfo(rarity).section}============="
			)
		}

		val container = meta.persistentDataContainer
		potionID?.let {
			val potionIDKey = NamespacedKey(plugin, "Potion_id")
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
			1,
			"§c治癒のポーション",
			10,
			false,
			lore = listOf(),
			"common",
			color = Color.RED,
			effects = listOf(
				PotionEffect(PotionEffectType.INSTANT_HEALTH, 1, 1),
				PotionEffect(PotionEffectType.BLINDNESS, 200, 2)
			)
		)
	}

	fun createStrengthPotion(): ItemStack {
		return createPotion(
			2,
			"§6力のポーション",
			10,
			false,
			lore = listOf(),
			"rare",
			301,
			color = Color.ORANGE,
			effects = listOf(PotionEffect(PotionEffectType.STRENGTH, 200, 1))
		)
	}

	fun createSpeedPotion(): ItemStack {
		return createPotion(
			3,
			"§b俊敏のポーション",
			10,
			false,
			lore = listOf(),
			"uncommon",
			302,
			color = Color.BLUE,
			effects = listOf(PotionEffect(PotionEffectType.SPEED, 200, 1))
		)
	}

	fun createGiantPotion(): ItemStack {
		return createPotion(
			4,
			"§a巨人のポーション",
			10,
			true,
			listOf("§a巨人 I (00:10)"),
			"rare",
			303,
			Color.GREEN,
			listOf(PotionEffect(PotionEffectType.SLOWNESS, 200, 1))
		)
	}

	fun createMidgetPotion(): ItemStack {
		return createPotion(
			5,
			"§e小人のポーション",
			10,
			true,
			listOf("§e小人 I (00:10)"),
			"rare",
			304,
			color = Color.YELLOW,
			effects = listOf(PotionEffect(PotionEffectType.SLOWNESS, 200, 1))
		)
	}
}