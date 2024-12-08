package com.github.yukkimoru.sdtm.trade.gui

import com.destroystokyo.paper.profile.ProfileProperty
import com.github.yukkimoru.sdtm.world.FactoryTool
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.plugin.java.JavaPlugin
import java.util.*

object Interface {
	private lateinit var plugin: JavaPlugin

	fun initialize(plugin: JavaPlugin) {
		this.plugin = plugin
	}

	fun shopUtility(): Inventory {
		val gui = createInventory("雑貨商人", 36)

		val Diamond = createItem(Material.DIAMOND, 1, "Click me!", TextColor.color(0x00FFFF))
		val Oak_Planks = createItem(Material.OAK_PLANKS, 1, "Archer Tower", TextColor.color(0x00FF00))
		val Warden = createPlayerHead(
			"Warden",
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNmMzY3NGIyZGRjMGVmN2MzOWUzYjljNmI1ODY3N2RlNWNmMzc3ZDJlYjA3M2YyZjNmZTUwOTE5YjFjYTRjOSJ9fX0=",
			TextColor.color(0xFF0000)
		)

		// Add the items to the inventory
		gui.setItem(2, Oak_Planks)
		gui.setItem(25, Diamond)
		gui.setItem(26, Warden) // Add the player head to the inventory

		return gui
	}

	fun shopPickaxe(): Inventory {
		val gui = createInventory("ツルハシの商人", 36)

		val factoryTool = FactoryTool(plugin)

		val tier1Pickaxe = factoryTool.createWoodenPickaxe()
		val tier2Pickaxe = factoryTool.createIronPickaxe()
		val tier3Pickaxe = factoryTool.createStonePickaxe()

		val tier1GemPickaxe = factoryTool.createRubyPickaxe()
		val tier2GemPickaxe = factoryTool.createSapphirePickaxe()

		gui.setItem(10, tier1Pickaxe)
		gui.setItem(11, tier2Pickaxe)
		gui.setItem(12, tier3Pickaxe)

		gui.setItem(19, tier1GemPickaxe)
		gui.setItem(20, tier2GemPickaxe)

		addFrames(gui, Material.BLACK_STAINED_GLASS_PANE)

		return gui
	}

	fun shopPotion(): Inventory {
		val gui = createInventory("ポーション商人", 27)

		val Diamond = createItem(Material.DIAMOND, 1, "Click me!", TextColor.color(0x00FFFF))
		val Oak_Planks = createItem(Material.OAK_PLANKS, 1, "Archer Tower", TextColor.color(0x00FF00))
		val Warden = createPlayerHead(
			"Warden",
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNmMzY3NGIyZGRjMGVmN2MzOWUzYjljNmI1ODY3N2RlNWNmMzc3ZDJlYjA3M2YyZjNmZTUwOTE5YjFjYTRjOSJ9fX0=",
			TextColor.color(0xFF0000)
		)

		// Add the items to the inventory
		gui.setItem(2, Oak_Planks)
		gui.setItem(25, Diamond)
		gui.setItem(26, Warden) // Add the player head to the inventory

		return gui
	}

	fun shopWeapon(): Inventory {
		val gui = createInventory("武器商人", 27)

		val Diamond = createItem(Material.DIAMOND, 1, "Click me!", TextColor.color(0x00FFFF))
		val Oak_Planks = createItem(Material.OAK_PLANKS, 1, "Archer Tower", TextColor.color(0x00FF00))
		val Warden = createPlayerHead(
			"Warden",
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNmMzY3NGIyZGRjMGVmN2MzOWUzYjljNmI1ODY3N2RlNWNmMzc3ZDJlYjA3M2YyZjNmZTUwOTE5YjFjYTRjOSJ9fX0=",
			TextColor.color(0xFF0000)
		)

		// Add the items to the inventory
		gui.setItem(2, Oak_Planks)
		gui.setItem(25, Diamond)
		gui.setItem(26, Warden) // Add the player head to the inventory

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

	private fun addFrames(gui: Inventory, material: Material) {
		for (i in 0..8) {
			gui.setItem(i, createItem(material, 1, "", TextColor.color(0x000000)))
		}
		gui.setItem(9, createItem(material, 1, "", TextColor.color(0x000000)))
		gui.setItem(18, createItem(material, 1, "", TextColor.color(0x000000)))
		gui.setItem(17, createItem(material, 1, "", TextColor.color(0x000000)))
		gui.setItem(26, createItem(material, 1, "", TextColor.color(0x000000)))
		for (i in 27..35) {
			gui.setItem(i, createItem(material, 1, "", TextColor.color(0x000000)))
		}
	}
}