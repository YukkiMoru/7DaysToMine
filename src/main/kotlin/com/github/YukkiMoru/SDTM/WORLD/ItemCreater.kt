package com.github.YukkiMoru.SDTM.WORLD

import org.bukkit.Material
import org.bukkit.inventory.ItemStack

class ItemCreater {

	public fun CreateItemStack(
		material: Material,
		amount: Int,
		name: String,
		lore: List<String>
	): ItemStack {
		val itemStack = ItemStack(material, amount)
		val itemMeta = itemStack.itemMeta
		itemMeta.setDisplayName(name)
		itemMeta.lore = lore
		itemStack.itemMeta = itemMeta
		return itemStack
	}
}