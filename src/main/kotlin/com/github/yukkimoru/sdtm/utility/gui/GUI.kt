package com.github.yukkimoru.sdtm.utility.gui

import com.destroystokyo.paper.profile.ProfileProperty
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import java.util.*

object GUI {
	fun PlatformGUI(): Inventory {
		val gui = createInventory("PlatformGUI", 27)

		val Diamond = createItem(Material.DIAMOND, 1, "Click me!", ChatColor.AQUA)
		val Oak_Planks = createItem(Material.OAK_PLANKS, 1, "Archer Tower", ChatColor.GREEN)
		val Warden = createPlayerHead(
			"Warden",
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNmMzY3NGIyZGRjMGVmN2MzOWUzYjljNmI1ODY3N2RlNWNmMzc3ZDJlYjA3M2YyZjNmZTUwOTE5YjFjYTRjOSJ9fX0=",
			ChatColor.RED
		)

		// Add the items to the inventory
		gui.setItem(2, Oak_Planks)
		gui.setItem(25, Diamond)
		gui.setItem(26, Warden) // Add the player head to the inventory

		return gui
	}

	private fun createInventory(name: String, size: Int): Inventory {
		return Bukkit.createInventory(null, size, name)
	}

	private fun createItem(material: Material, amount: Int, displayName: String, color: ChatColor?): ItemStack {
		val item = ItemStack(material, amount)
		val meta = item.itemMeta
		meta.setDisplayName(color.toString() + displayName)
		item.setItemMeta(meta)
		return item
	}

	private fun createPlayerHead(name: String, value: String, color: ChatColor): ItemStack {
		val playerHead = ItemStack(Material.PLAYER_HEAD)
		val skullMeta = playerHead.itemMeta as SkullMeta

		val profile = Bukkit.createProfile(UUID.randomUUID(), null)
		profile.setProperty(ProfileProperty("textures", value))
		skullMeta.playerProfile = profile

		skullMeta.setDisplayName(color.toString() + name)
		playerHead.setItemMeta(skullMeta)

		return playerHead
	}
}