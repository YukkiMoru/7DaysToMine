package com.github.YukkiMoru.SDTM.WORLD

import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.inventory.ItemStack
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

class ItemCreater(private val plugin: JavaPlugin) {

	public fun CreateItemStack(
		material: Material,
		amount: Int,
		name: String,
		lore: List<String>,
		rarity: String
	): ItemStack {
		val itemStack = ItemStack(material, amount)
		val itemMeta = itemStack.itemMeta
		itemMeta.setDisplayName(name)
		itemMeta.lore = lore + getRarityDisplayName(rarity)

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