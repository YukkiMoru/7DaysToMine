package com.github.yukkimoru.sdtm.utility.items

import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.EquipmentSlot

class EnderPack {
	class OpenEnderChest : Listener {

		@EventHandler
		fun onPlayerInteract(event: PlayerInteractEvent) {
			val player = event.player
			val itemInHand = player.inventory.itemInMainHand

			// 右クリックかつメインハンドでの操作のみを対象とする
			if (event.hand == EquipmentSlot.HAND && itemInHand.type == Material.RECOVERY_COMPASS) {
				player.openInventory(player.enderChest)
				event.isCancelled = true // イベントをキャンセルして通常の動作を防ぐ
			}
		}
	}
}