package com.github.yukkimoru.sdtm.utility.gui

import com.github.yukkimoru.sdtm.SDTM
import com.github.yukkimoru.sdtm.tower.Construction
import com.github.yukkimoru.sdtm.tower.ListenerBlock
import com.github.yukkimoru.sdtm.tower.ListenerBlockManager
import com.github.yukkimoru.sdtm.tower.Tower
import org.bukkit.Location
import org.bukkit.entity.ArmorStand
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent
import org.bukkit.plugin.java.JavaPlugin

class GUIReceiver(private val listenerBlock: ListenerBlock) : Listener {

	private val towerManager = SQLManagerTower.getInstance()
	private var towerID = towerManager.getLastTowerID() + 1

	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		if (event.inventory.viewers.contains(event.whoClicked)) {
			when (event.view.title) {
				"PlatformGUI" -> handlePlatformGUI(event)
				"TowerGUI" -> handleTowerGUI(event)
			}
		}
	}

	private fun handlePlatformGUI(event: InventoryClickEvent) {
		event.isCancelled = true
		val player = event.whoClicked as Player

		player.sendMessage("You clicked at slot ${event.slot}")
		// get edgelocation from ListenerBlockManager
		val edgeLocation = ListenerBlockManager.getEdgeLocation(player)

		when (event.slot) {
			2 -> {
				if (edgeLocation != null) {
					player.sendMessage("edgeLocation from ListenerBlockManager: $edgeLocation")
					BuildingTower("archer_1", player, edgeLocation)
				} else {
					player.sendMessage("EdgeLocation is null")
				}
			}

			3 -> { /* WIP: make other tower e.g., mage, warrior, and frozen */
			}

			25 -> { /* Code to execute when Diamond is clicked */
			}

			26 -> { /* Code to execute when Warden is clicked */
			}
		}
	}

	private fun handleTowerGUI(event: InventoryClickEvent) {
		event.isCancelled = true
		val player = event.whoClicked as Player
		val construction = Construction()

		player.sendMessage("You clicked at slot ${event.slot}")

		when (event.slot) {
			8 -> {
				val towerIdToRemove = listenerBlock.getCurrentTowerID()
				construction.removeStructure(towerIdToRemove)
				towerManager.removeTower(towerIdToRemove)
				towerManager.removeTowerCoordinates(towerIdToRemove)
				Tower.removeTowerStand(towerIdToRemove)
				player.sendMessage("TowerID: $towerIdToRemove was removed")
			}

			49 -> {
				val clickedTowerID = listenerBlock.getCurrentTowerID()
				towerManager.upgradeTower(clickedTowerID)
				val newGui = InventoryGUI.towerGUI(clickedTowerID)
				player.openInventory(newGui)
				player.playSound(player.location, "minecraft:block.anvil.destroy", 1.0f, 0.5f)

				val towerData = towerManager.getTowerDatabase(clickedTowerID)
				val edgeLocation = ListenerBlockManager.getEdgeLocation(player)
				if (edgeLocation != null) {
					val structureName = "archer_${towerData?.level}"
					player.sendMessage("StructureName: $structureName")
					construction.summonStructure(edgeLocation, structureName)
					player.sendMessage("TowerID: $clickedTowerID was upgraded")

					Tower.removeTowerStand(clickedTowerID)
					val size = construction.getSizeStructure(structureName)
					val spawnLocation = edgeLocation.clone().apply {
						this.x += (size.x / 2) - 0.5
						this.y += size.y + 1.0
						this.z += (size.z / 2) - 0.5
					}
					val newArmorStand = edgeLocation.world!!.spawn(spawnLocation, ArmorStand::class.java)
					Tower(newArmorStand, 1.0, 1L, 10.0, clickedTowerID, JavaPlugin.getPlugin(SDTM::class.java))
				}
			}
		}
	}

	private fun BuildingTower(towerName: String, player: Player, edgeLocation: Location) {
		val construction = Construction()
		construction.summonStructure(edgeLocation, towerName)
		val size = construction.getSizeStructure(towerName)
		val spawnLocation = edgeLocation.clone().apply {
			this.x += (size.x / 2) - 0.5
			this.y += size.y + 1.0
			this.z += (size.z / 2) - 0.5
		}
		val armorStand = edgeLocation.world!!.spawn(spawnLocation, ArmorStand::class.java)
		Tower(armorStand, 100.0, 1L, 100.0, towerID, JavaPlugin.getPlugin(SDTM::class.java))

		towerManager.writeTowerDatabase(towerID, "Archer", 3, 1)
		player.sendMessage("Count: $towerID")
		player.sendMessage("Tower size: ${size.x} ${size.y} ${size.z}")
		towerManager.writeCoordinates(towerID, edgeLocation, size)
		player.sendMessage("Tower constructed")
		towerID++
		player.playSound(player.location, "minecraft:block.anvil.destroy", 1.0f, 0.5f)
	}
}