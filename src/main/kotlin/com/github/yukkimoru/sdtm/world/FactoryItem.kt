package com.github.yukkimoru.sdtm.world

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class FactoryItem(private val plugin: JavaPlugin) {

	fun createItemStack(
		material: Material,
		amount: Int,
		name: String,
		lore: List<String>,
		rarity: String,
		customModelData: Int? = null
	): ItemStack {
		val itemStack = ItemStack(material, amount)
		val itemMeta = itemStack.itemMeta
		itemMeta.displayName(Component.text(name).decoration(TextDecoration.ITALIC, false))
		itemMeta.lore(lore.map { Component.text(it).decoration(TextDecoration.ITALIC, false) } + Component.text(
			getRarityDisplayName(rarity)
		).decoration(TextDecoration.ITALIC, false))

		// Add rarity to the item
		val container = itemMeta.persistentDataContainer
		val key = NamespacedKey(plugin, "rarity")
		container.set(key, PersistentDataType.STRING, rarity)

		// Add custom model data if provided
		customModelData?.let {
			itemMeta.setCustomModelData(it)
		}

		itemStack.itemMeta = itemMeta
		return itemStack
	}

	private fun getRarityDisplayName(rarity: String): String {
		return when (rarity.lowercase(Locale.getDefault())) {
			"common" -> "§f§lコモン"
			"uncommon" -> "§a§lアンコモン"
			"rare" -> "§9§lレア"
			"epic" -> "§5§lエピック"
			"legendary" -> "§6§lレジェンド"
			"mythic" -> "§d§lミシック"
			else -> "§4§k**§r§4§lNULL§r§4§k**"
		}
	}

	// カスタムモデルデータ(ID)を取得、ない場合はnullを返す
	fun getCustomModelData(item: ItemStack): Int? {
		val meta: ItemMeta = item.itemMeta ?: return null
		return if (meta.hasCustomModelData()) meta.customModelData else null
	}

	fun isItemWithCustomModelData(item: ItemStack, modelData: Int): Boolean {
		return getCustomModelData(item) == modelData
	}
}