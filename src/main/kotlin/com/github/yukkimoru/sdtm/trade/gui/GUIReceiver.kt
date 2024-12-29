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
			9 -> purchasePotion(event, "healing/1")
			10 -> purchasePotion(event, "healing/2")
			11 -> purchasePotion(event, "healing/3")
			12 -> purchasePotion(event, "healing/4")
			18 -> purchasePotion(event, "strength/1")
			19 -> purchasePotion(event, "strength/2")
			20 -> purchasePotion(event, "strength/3")
			21 -> purchasePotion(event, "strength/4")
			27 -> purchasePotion(event, "speed/1")
			28 -> purchasePotion(event, "speed/2")
			29 -> purchasePotion(event, "speed/3")
			30 -> purchasePotion(event, "speed/4")
			36 -> purchasePotion(event, "giant/1")
			37 -> purchasePotion(event, "giant/2")
			38 -> purchasePotion(event, "giant/3")
			39 -> purchasePotion(event, "giant/4")
			45 -> purchasePotion(event, "midget/1")
			46 -> purchasePotion(event, "midget/2")
			47 -> purchasePotion(event, "midget/3")
			48 -> purchasePotion(event, "midget/4")
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

	private fun purchasePotion(event: InventoryClickEvent, potionNameLevel: String) {
		val world = Bukkit.getWorld("world")
		val player = event.whoClicked as Player
		val playerInventory = player.inventory
		val potionData = potionFactory.getPotionInfo(potionNameLevel)
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
			playerInventory.addItem(potionFactory.createPotion(potionNameLevel, false))
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