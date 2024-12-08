package com.github.yukkimoru.sdtm.trade.gui

import com.github.yukkimoru.sdtm.SDTM
import com.github.yukkimoru.sdtm.world.FactoryTool
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.plugin.java.JavaPlugin

class GUIReceiver() : Listener {

	@Suppress("DEPRECATION")
	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		if (event.inventory.viewers.contains(event.whoClicked)) {
			when (event.view.title) {
//				"雑貨商人" -> handleUtilityShopGUI(event)
				"ツルハシの商人" -> handlePickaxeShopGUI(event)
//				"ポーション商人" -> handlePotionShopGUI(event)
//				"武器商人" -> handleWeaponShopGUI(event)
			}
		}
	}

	private fun handlePickaxeShopGUI(event: InventoryClickEvent) {
		event.isCancelled = true
		val player = event.whoClicked as Player

//		player.sendMessage("You clicked at slot ${event.slot}")

		when (event.slot) {
			10 -> handlePickaxePurchase(event, mapOf(Material.EMERALD to 10, Material.GOLD_INGOT to 10), "wooden")
			11 -> handlePickaxePurchase(event, mapOf(Material.EMERALD to 10, Material.GOLD_INGOT to 1), "stone")
		}
	}

	private fun handlePickaxePurchase(event: InventoryClickEvent, Cost: Map<Material, Int>, pickaxeType: String) {
		event.isCancelled = true
		val player = event.whoClicked as Player
		val playerInventory = player.inventory
		val hasAllMaterials = Cost.all { (material, amount) ->
			playerInventory.all(material).values.sumOf { it.amount } >= amount
		}
		val world = Bukkit.getWorld("world")
		if (hasAllMaterials) {
			Cost.forEach { (material, amount) ->
				playerInventory.removeItem(ItemStack(material, amount))
			}
			val factoryTool = FactoryTool(JavaPlugin.getPlugin(SDTM::class.java))
			val pickaxe = when (pickaxeType) {
				"wooden" -> factoryTool.createWoodenPickaxe()
				"stone" -> factoryTool.createStonePickaxe()
				else -> return
			}
			playerInventory.addItem(pickaxe)
			world?.playSound(player.location, "minecraft:block.note_block.pling", 1.2f, 2.0f)
		} else {
			world?.playSound(player.location, "entity.enderman.teleport", 1.2f, 0.1f)
			player.sendMessage("必要な材料が不足しています!")
		}
	}
}