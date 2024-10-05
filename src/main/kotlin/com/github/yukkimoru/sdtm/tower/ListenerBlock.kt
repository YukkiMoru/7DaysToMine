package com.github.yukkimoru.sdtm.tower

import com.github.yukkimoru.sdtm.utility.gui.InventoryGUI
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot
import org.bukkit.inventory.Inventory

class ListenerBlock : Listener {
	private var currentTowerID: Int = 0
	var edgeLocation: Location? = null
		private set

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		val player = event.player
		if (event.action == Action.RIGHT_CLICK_BLOCK && event.hand == EquipmentSlot.HAND &&
			!player.isSneaking
		) {
			// プラットフォームかどうかを判定
			PlatformClick(event.clickedBlock!!.type, event)
			// タワーかどうかを判定
			TowerClick(event)
		}
	}

	private fun PlatformClick(clickedBlock: Material, event: PlayerInteractEvent) {
		val managePlatform: ManagePlatform = ManagePlatform()
		if (clickedBlock == Material.CHERRY_PLANKS) {
			if (event.player.inventory.itemInMainHand.type == Material.AIR) {
				edgeLocation =
					managePlatform.Platform(event.clickedBlock!!.location, 3, 3, Material.CHERRY_PLANKS, event)
				if (edgeLocation != null) {
					val player = event.player
					val gui: Inventory = InventoryGUI.platformGUI()
					player.openInventory(gui)
				}
			}
		}
	}

	private fun TowerClick(event: PlayerInteractEvent) {
		val clickedBlockLocation = event.clickedBlock!!.location
		val sqliteManagerTower: ManagerTower = ManagerTower()
		currentTowerID = sqliteManagerTower.GetTowerID(clickedBlockLocation)
		if (currentTowerID != 0) {
			val gui: Inventory = InventoryGUI.towerGUI(currentTowerID)
			event.player.openInventory(gui)
		}
	}

	fun getCurrentTowerID(): Int {
		return currentTowerID
	}
}