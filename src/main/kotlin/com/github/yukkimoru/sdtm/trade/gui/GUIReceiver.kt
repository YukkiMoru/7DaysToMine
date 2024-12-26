package com.github.yukkimoru.sdtm.trade.gui

import com.github.yukkimoru.sdtm.SDTM
import com.github.yukkimoru.sdtm.trade.pickaxe.ToolFactory
import com.github.yukkimoru.sdtm.trade.potion.PotionFactory
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class GUIReceiver() : Listener {
	val toolFactory = ToolFactory(JavaPlugin.getPlugin(SDTM::class.java))
	val potionFactory = PotionFactory(JavaPlugin.getPlugin(SDTM::class.java))

	@Suppress("DEPRECATION")
	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		if (event.inventory.viewers.contains(event.whoClicked)) {
			when (event.view.title) {
				"ツルハシの商人" -> handlePickaxeShopGUI(event)
				"ポーションの商人" -> handlePotionShopGUI(event)
			}
		}
	}

	private fun handlePickaxeShopGUI(event: InventoryClickEvent) {
		event.isCancelled = true
		when (event.slot) {
			10 -> purchasePickaxe(event, 200)
			11 -> purchasePickaxe(event, 201)
			12 -> purchasePickaxe(event, 202)
			19 -> purchasePickaxe(event, 300)
			20 -> purchasePickaxe(event, 301)
		}
	}

	private fun handlePotionShopGUI(event: InventoryClickEvent) {
		event.isCancelled = true
		when (event.slot) {
			10 -> purchasePotion(event, "healing", 1)
			11 -> purchasePotion(event, "strength", 1)
			12 -> purchasePotion(event, "speed", 1)
			13 -> purchasePotion(event, "giant", 1)
			14 -> purchasePotion(event, "midget", 1)
		}
	}

	private fun purchasePickaxe(event: InventoryClickEvent, customModelID: Int) {
		val world = Bukkit.getWorld("world")
		val player = event.whoClicked as Player
		val playerInventory = player.inventory
		val costMaterial = toolFactory.pickaxes[customModelID]?.pickaxeCosts ?: emptyMap()
		if (isInventoryFull(playerInventory)) {
			player.sendMessage("インベントリがいっぱいです!")
			world?.playSound(player.location, "entity.enderman.teleport", 1.2f, 0.1f)
			return
		}
		val hasAllMaterials = costMaterial.all { (material: Material, amount: Int) ->
			playerInventory.all(material).values.sumOf { it.amount } >= amount
		}
		if (hasAllMaterials) {
			costMaterial.forEach { (material: Material, amount: Int) ->
				playerInventory.removeItem(ItemStack(material, amount))
			}
			playerInventory.addItem(toolFactory.createPickaxe(customModelID, false))
			world?.playSound(player.location, "minecraft:block.note_block.pling", 1.2f, 2.0f)
		} else {
			world?.playSound(player.location, "entity.enderman.teleport", 1.2f, 0.1f)
			player.sendMessage("必要な材料が不足しています!")
		}
	}

	private fun purchasePotion(event: InventoryClickEvent, potionName: String, potionLevel: Int) {
		val world = Bukkit.getWorld("world")
		val player = event.whoClicked as Player
		val playerInventory = player.inventory
		val potionData = potionFactory.getPotionInfo(potionName, potionLevel)
		val costMaterial = potionData?.potionCosts ?: emptyMap()
		if (isInventoryFull(playerInventory)) {
			player.sendMessage("インベントリがいっぱいです!")
			world?.playSound(player.location, "entity.enderman.teleport", 1.2f, 0.1f)
			return
		}
		val hasAllMaterials = costMaterial.all { (material: Material, amount: Int) ->
			playerInventory.all(material).values.sumOf { it.amount } >= amount
		}
		if (hasAllMaterials) {
			costMaterial.forEach { (material: Material, amount: Int) ->
				playerInventory.removeItem(ItemStack(material, amount))
			}
			playerInventory.addItem(potionFactory.createPotion(potionName, potionLevel, false))
			world?.playSound(player.location, "minecraft:block.note_block.pling", 1.2f, 2.0f)
		} else {
			world?.playSound(player.location, "entity.enderman.teleport", 1.2f, 0.1f)
			player.sendMessage("必要な材料が不足しています!")
		}
	}

	private fun isInventoryFull(playerInventory: Inventory): Boolean {
		return playerInventory.firstEmpty() == -1
	}
}