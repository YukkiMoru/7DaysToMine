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

	private var debugMode = true // デバッグモードのフラグ

	@EventHandler
	fun onPlayerInteract(event: PlayerInteractEvent) {
		val player = event.player
		if (event.action == Action.RIGHT_CLICK_BLOCK && event.hand == EquipmentSlot.HAND && !player.isSneaking) {
			// プラットフォームかどうかを判定
			PlatformClick(event.clickedBlock!!.type, event)
			// タワーかどうかを判定
			TowerClick(event)
		}
	}

	private fun PlatformClick(clickedBlock: Material, event: PlayerInteractEvent) {
		if (clickedBlock == Material.CHERRY_PLANKS) {
			if (event.player.inventory.itemInMainHand.type == Material.AIR) {
				edgeLocation = Platform(event.clickedBlock!!.location, 3, 3, Material.CHERRY_PLANKS, event)
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

	fun setDebugMode(debugMode: Boolean) {
		this.debugMode = debugMode
	}

	fun Platform(
		location: Location,
		sizeX: Int,
		sizeZ: Int,
		material: Material,
		event: PlayerInteractEvent
	): Location? {
		// デバッグメッセージを追加
		if (debugMode) {
			event.player.sendMessage("Platform method called with location: $location, sizeX: $sizeX, sizeZ: $sizeZ, material: $material")
		}

		// クリックされた場所の座標
		val x = location.x.toInt()
		val y = location.y.toInt()
		val z = location.z.toInt()

		// 東西南北
		var west = 0
		var east = 0
		var south = 0
		var north = 0

		// 東西南北のブロックの数を数える
		while (location.world.getBlockAt(x - 1 + west, location.y.toInt(), z).type == material) west--
		while (location.world.getBlockAt(x + 1 + east, location.y.toInt(), z).type == material) east++
		while (location.world.getBlockAt(x, location.y.toInt(), z + 1 + south).type == material) south++
		while (location.world.getBlockAt(x, location.y.toInt(), z - 1 + north).type == material) north--

		// 東西南北のブロックの数を数える
		val PlatformX = east - west + 1
		val PlatformZ = south - north + 1

		// Edge座標
		val EdgeX = x + west
		val EdgeZ = z + north

		if (debugMode) {
			val player = event.player
			player.sendMessage("West: " + -west)
			player.sendMessage("East: $east")
			player.sendMessage("South: $south")
			player.sendMessage("North: " + -north)
			player.sendMessage("Platform_Size_X: $PlatformX")
			player.sendMessage("Platform_Size_Z: $PlatformZ")
			player.sendMessage("EdgeX: $EdgeX, EdgeZ: $EdgeZ")
			player.sendMessage("Clicked " + (location.x) + " " + (location.y) + " " + (location.z))
		}

		// プラットフォームの判定
		val player = event.player
		if (PlatformX == sizeX && PlatformZ == sizeZ) {
			for (i in EdgeX until (EdgeX + PlatformX)) {
				for (j in EdgeZ until (EdgeZ + PlatformZ)) {
					if (debugMode) player.sendMessage("Block was checked at " + i + " " + location.y.toInt() + " " + j)
					if (location.world.getBlockAt(
							Location(
								location.world!!,
								i.toDouble(),
								location.y,
								j.toDouble()
							)
						).type != material
					) {
						if (debugMode) {
							player.sendMessage(i.toString() + " " + location.y.toInt() + " " + j + " is wrong material!")
							player.sendMessage("This Platform is not filled with $material!")
						}
						return null
					}
				}
			}
		} else {
			if (debugMode) {
				player.sendMessage("This Platform is not filled with $material!")
			}
			return null
		}
		if (debugMode) {
			player.sendMessage("You clicked part of $sizeX * $sizeZ $material!")
		}
		edgeLocation = Location(location.world!!, (EdgeX + 1).toDouble(), location.y, (EdgeZ + 1).toDouble())
		if (debugMode) {
			player.sendMessage("EdgeLocation set to: $edgeLocation")
		}
		return edgeLocation
	}
}