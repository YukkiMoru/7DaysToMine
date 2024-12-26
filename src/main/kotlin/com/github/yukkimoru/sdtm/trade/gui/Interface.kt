package com.github.yukkimoru.sdtm.trade.gui

import com.destroystokyo.paper.profile.ProfileProperty
import com.github.yukkimoru.sdtm.trade.pickaxe.ToolFactory
import com.github.yukkimoru.sdtm.trade.potion.PotionFactory
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

@Suppress("SpellCheckingInspection")
object Interface {
	private lateinit var plugin: JavaPlugin

	fun initialize(plugin: JavaPlugin) {
		this.plugin = plugin
	}

	fun shopPickaxe(): Inventory {
		val inventorySize = 36
		val gui = createInventory("ツルハシの商人", inventorySize)

		val toolFactory = ToolFactory(plugin)

		gui.setItem(10, toolFactory.createPickaxe(200, true))
		gui.setItem(11, toolFactory.createPickaxe(201, true))
		gui.setItem(12, toolFactory.createPickaxe(202, true))

		gui.setItem(19, toolFactory.createPickaxe(300, true))
		gui.setItem(20, toolFactory.createPickaxe(301, true))

		addFrames(gui, Material.BLACK_STAINED_GLASS_PANE, inventorySize)

		return gui
	}

	fun shopPotion(): Inventory {
		val inventorySize = 27
		val gui = createInventory("ポーションの商人", inventorySize)

		val potionFactory = PotionFactory(plugin)

		gui.setItem(10, potionFactory.createPotion("healing", 1, true))
		gui.setItem(11, potionFactory.createPotion("strength", 1, true))
		gui.setItem(12, potionFactory.createPotion("speed", 1, true))
		gui.setItem(13, potionFactory.createPotion("giant", 1, true))
		gui.setItem(14, potionFactory.createPotion("midget", 1, true))

		addFrames(gui, Material.BLACK_STAINED_GLASS_PANE, inventorySize)

		return gui
	}

	fun shopUtility(): Inventory {
		val gui = createInventory("雑貨商人", 36)

		val diamond = createItem(Material.DIAMOND, 1, "Click me!", TextColor.color(0x00FFFF))
		val oakPlanks = createItem(Material.OAK_PLANKS, 1, "Archer Tower", TextColor.color(0x00FF00))
		val warden = createPlayerHead(
			"Warden",
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNmMzY3NGIyZGRjMGVmN2MzOWUzYjljNmI1ODY3N2RlNWNmMzc3ZDJlYjA3M2YyZjNmZTUwOTE5YjFjYTRjOSJ9fX0=",
			TextColor.color(0xFF0000)
		)

		// Add the items to the inventory
		gui.setItem(2, oakPlanks)
		gui.setItem(25, diamond)
		gui.setItem(26, warden) // Add the player head to the inventory

		return gui
	}

	fun shopWeapon(): Inventory {
		val gui = createInventory("武器商人", 27)

		val diamond = createItem(Material.DIAMOND, 1, "Click me!", TextColor.color(0x00FFFF))
		val oakPlanks = createItem(Material.OAK_PLANKS, 1, "Archer Tower", TextColor.color(0x00FF00))
		val warden = createPlayerHead(
			"Warden",
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNmMzY3NGIyZGRjMGVmN2MzOWUzYjljNmI1ODY3N2RlNWNmMzc3ZDJlYjA3M2YyZjNmZTUwOTE5YjFjYTRjOSJ9fX0=",
			TextColor.color(0xFF0000)
		)

		// Add the items to the inventory
		gui.setItem(2, oakPlanks)
		gui.setItem(25, diamond)
		gui.setItem(26, warden) // Add the player head to the inventory

		return gui
	}

	private fun createInventory(name: String, size: Int): Inventory {
		return Bukkit.createInventory(null, size, Component.text(name))
	}

	private fun createItem(material: Material, amount: Int, displayName: String, color: TextColor?): ItemStack {
		val item = ItemStack(material, amount)
		val meta = item.itemMeta
		meta.displayName(Component.text(displayName).color(color))
		item.setItemMeta(meta)
		return item
	}

	private fun createPlayerHead(name: String, value: String, color: TextColor): ItemStack {
		val playerHead = ItemStack(Material.PLAYER_HEAD)
		val skullMeta = playerHead.itemMeta as SkullMeta

		val profile = Bukkit.createProfile(UUID.randomUUID(), null)
		profile.setProperty(ProfileProperty("textures", value))
		skullMeta.playerProfile = profile

		skullMeta.displayName(Component.text(name).color(color))
		playerHead.setItemMeta(skullMeta)

		return playerHead
	}

	private fun addFrames(gui: Inventory, material: Material, size: Int) {
//		*********
//		*       *
//		*       *
//		*       *
//		*       *
//		*********
//		e.g) column = 6 , effectiveColumn = 4

		for (i in 0..8) {
			gui.setItem(i, createItem(material, 1, "", TextColor.color(0x000000)))
		}
		var effectiveColumn = size / 9 - 2
		var row = 9
		while (effectiveColumn > 0) {
			gui.setItem(row, createItem(material, 1, "", TextColor.color(0x000000)))
			gui.setItem(row + 8, createItem(material, 1, "", TextColor.color(0x000000)))
			effectiveColumn--
			row += 9
		}
//		if(effectiveColumn <= 1) {
//			gui.setItem(9, createItem(material, 1, "", TextColor.color(0x000000)))
//			gui.setItem(17, createItem(material, 1, "", TextColor.color(0x000000)))
//		}
//		if(effectiveColumn <= 4) {
//			gui.setItem(18, createItem(material, 1, "", TextColor.color(0x000000)))
//			gui.setItem(26, createItem(material, 1, "", TextColor.color(0x000000)))
//		}
//		if(effectiveColumn <= 5) {
//			gui.setItem(27, createItem(material, 1, "", TextColor.color(0x000000)))
//			gui.setItem(35, createItem(material, 1, "", TextColor.color(0x000000)))
//		}
//
		for (i in row..row + 8) {
			gui.setItem(i, createItem(material, 1, "", TextColor.color(0x000000)))
		}
	}
}