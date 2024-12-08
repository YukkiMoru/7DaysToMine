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
			10 -> {
				val playerInventory = player.inventory
				val emeralds = playerInventory.all(Material.EMERALD).values.sumOf { it.amount }
				val world = Bukkit.getWorld("world")
				if (emeralds >= 10) {
					playerInventory.removeItem(ItemStack(Material.EMERALD, 10))

					// Create the FactoryTool instance
					val factoryTool = FactoryTool(JavaPlugin.getPlugin(SDTM::class.java))

					// Create the Tier 1 Pickaxe
					val tier1Pickaxe = factoryTool.createWoodenPickaxe()

					// Add the created pickaxe to the player's inventory
					playerInventory.addItem(tier1Pickaxe)
					world?.playSound(player.location, "minecraft:block.note_block.pling", 1.2f, 2.0f)
				} else {
					world?.playSound(player.location, "entity.enderman.teleport", 1.2f, 0.1f)
					player.sendMessage("10このエメラルドが必要です!")
				}
			}

			11 -> {
				val playerInventory = player.inventory
				val emeralds = playerInventory.all(Material.EMERALD).values.sumOf { it.amount }
				val world = Bukkit.getWorld("world")
				if (emeralds >= 20) {
					playerInventory.removeItem(ItemStack(Material.EMERALD, 20))

					// Create the FactoryTool instance
					val factoryTool = FactoryTool(JavaPlugin.getPlugin(SDTM::class.java))

					// Create the Tier 2 Pickaxe
					val tier2Pickaxe = factoryTool.createStonePickaxe()

					// Add the created pickaxe to the player's inventory
					playerInventory.addItem(tier2Pickaxe)
					world?.playSound(player.location, "minecraft:block.note_block.pling", 1.2f, 2.0f)
				} else {
					world?.playSound(player.location, "entity.enderman.teleport", 1.2f, 0.1f)
					player.sendMessage("20このエメラルドが必要です!")
				}
			}
		}
	}
}