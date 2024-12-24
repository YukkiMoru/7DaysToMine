package com.github.yukkimoru.sdtm.utility

import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.ItemMeta
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class ItemFactory(private val plugin: JavaPlugin) {

	fun createItemStack(
		material: Material,
		amount: Int,
		name: String,
		lore: List<String>,
		rarity: String,
		customModelID: Int? = null
	): ItemStack {
		val itemStack = ItemStack(material, amount)
		val itemMeta = itemStack.itemMeta
		itemMeta.displayName(Component.text(name).decoration(TextDecoration.ITALIC, false))
		itemMeta.lore(lore.map { Component.text(it).decoration(TextDecoration.ITALIC, false) } + Component.text(
			RarityUtil.getInfo(rarity).section
		).decoration(TextDecoration.ITALIC, false))

		// Add rarity to the item
		val container = itemMeta.persistentDataContainer
		val key = NamespacedKey(plugin, "rarity")
		container.set(key, PersistentDataType.STRING, rarity)

		// Add custom model data if provided
		customModelID?.let {
			itemMeta.setCustomModelData(it)
		}

		itemStack.itemMeta = itemMeta
		return itemStack
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