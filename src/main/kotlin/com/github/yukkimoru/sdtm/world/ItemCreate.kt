package com.github.yukkimoru.sdtm.world

import net.kyori.adventure.text.Component
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class ItemCreate(private val plugin: JavaPlugin) {

	fun createItemStack(
		material: Material,
		amount: Int,
		name: String,
		lore: List<String>,
		rarity: String
	): ItemStack {
		val itemStack = ItemStack(material, amount)
		val itemMeta = itemStack.itemMeta
		itemMeta.displayName(Component.text(name))
		itemMeta.lore(lore.map { Component.text(it) } + Component.text(getRarityDisplayName(rarity)))

		// Add rarity to the item
		val container = itemMeta.persistentDataContainer
		val key = NamespacedKey(plugin, "rarity")
		container.set(key, PersistentDataType.STRING, rarity)

		itemStack.itemMeta = itemMeta
		return itemStack
	}

	private fun getRarityDisplayName(rarity: String): String {
		return when (rarity.lowercase(Locale.getDefault())) {
			"common" -> "§fコモン"
			"uncommon" -> "§aアンコモン"
			"rare" -> "§9レア"
			"epic" -> "§5エピック"
			"legendary" -> "§6レジェンド"
			"mythic" -> "§dミシック"
			else -> "§fコモン"
		}
	}
}