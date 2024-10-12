package com.github.yukkimoru.sdtm.utility.gui

import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.inventory.InventoryClickEvent

class GUIReceiver() : Listener {


	@EventHandler
	fun onInventoryClick(event: InventoryClickEvent) {
		if (event.inventory.viewers.contains(event.whoClicked)) {
			when (event.view.title) {
				"PlatformGUI" -> handlePlatformGUI(event)
			}
		}
	}

	private fun handlePlatformGUI(event: InventoryClickEvent) {
		event.isCancelled = true
		val player = event.whoClicked as Player

		player.sendMessage("You clicked at slot ${event.slot}")

		when (event.slot) {
			2 -> {

			}

			25 -> {
				// Code to execute when Diamond is clicked
			}

			26 -> {
				// Code to execute when Warden is clicked
			}
		}
	}
}