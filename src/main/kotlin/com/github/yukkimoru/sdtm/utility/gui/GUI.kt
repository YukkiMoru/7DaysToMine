package org.moru.tower_defense

import com.destroystokyo.paper.profile.ProfileProperty
import com.github.yukkimoru.sdtm.SDTM
import com.github.yukkimoru.sdtm.tower.SQLManagerTower
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Material
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.inventory.Inventory
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.meta.SkullMeta
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.util.*

object InventoryGUI {
	fun platformGUI(): Inventory {
		val gui = createInventory("PlatformGUI", 27)

		val diamond = createItem(Material.DIAMOND, 1, "Click me!", ChatColor.AQUA)
		val oakPlanks = createItem(Material.OAK_PLANKS, 1, "Archer Tower", ChatColor.GREEN)
		val warden = createPlayerHead(
			"Warden",
			"eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvNmNmMzY3NGIyZGRjMGVmN2MzOWUzYjljNmI1ODY3N2RlNWNmMzc3ZDJlYjA3M2YyZjNmZTUwOTE5YjFjYTRjOSJ9fX0=",
			ChatColor.RED
		)

		// Add the items to the inventory
		gui.setItem(2, oakPlanks)
		gui.setItem(25, diamond)
		gui.setItem(26, warden) // Add the player head to the inventory

		return gui
	}

	fun towerGUI(towerID: Int): Inventory {
		val configFile = File(JavaPlugin.getPlugin(SDTM::class.java).dataFolder, "archer.yml")
		val config = YamlConfiguration.loadConfiguration(configFile)

		val sqliteManagerTower = SQLManagerTower.getInstance()
		val towerData = sqliteManagerTower.getTowerDatabase(towerID)

		Bukkit.broadcastMessage("TowerID: ${towerData?.TowerID}")
		Bukkit.broadcastMessage("TowerName: ${towerData?.TowerName}")
		Bukkit.broadcastMessage("TowerType: ${towerData?.TowerType}")
		Bukkit.broadcastMessage("Level: ${towerData?.level}")

		val gui = createInventory("TowerGUI", 54)

		val redGlassPanel = createItem(Material.RED_STAINED_GLASS_PANE, 1, "Red Glass Panel", ChatColor.RED)
		val greenGlassPanel = createItem(Material.GREEN_STAINED_GLASS_PANE, 1, "Green Glass Panel", ChatColor.GREEN)
		val grayGlassPanel = createItem(Material.GRAY_STAINED_GLASS_PANE, 1, "", null)
		val goldIngot = createItem(Material.GOLD_INGOT, 1, "Upgrade", ChatColor.GOLD)
		val barrier = createItem(Material.BARRIER, 1, "Remove", ChatColor.RED)

		gui.setItem(49, goldIngot)
		gui.setItem(8, barrier)

		if (towerData?.TowerName == "Archer") {
			val paths = config.getMapList("archer")

			for (path in paths) {
				val levels = path["levels"] as List<Map<String, Any>>

				for (level in levels) {
					if (level["level"] == towerData.level) {
						val archer = ItemStack(Material.BOW, 1)
						val meta = archer.itemMeta

						meta?.setDisplayName("${ChatColor.WHITE}Archer Level ${level["level"]}")
						meta?.lore = listOf(
							"${ChatColor.WHITE}Damage: ${level["damage"]}",
							"${ChatColor.WHITE}Attack Speed: ${level["attackspeed"]}",
							"${ChatColor.WHITE}Range: ${level["range"]}",
							"${ChatColor.WHITE}Skills: ${level["skills"]}"
						)

						archer.itemMeta = meta
						gui.setItem(4, archer)
					}
				}
			}
			val slots = intArrayOf(27, 28, 29, 30, 31, 32, 33, 34, 35)
			for (i in 0 until towerData.level) {
				gui.setItem(slots[i], greenGlassPanel)
			}
			for (i in towerData.level until slots.size) {
				gui.setItem(slots[i], redGlassPanel)
			}
		}

		return gui
	}

	private fun createInventory(name: String, size: Int): Inventory {
		return Bukkit.createInventory(null, size, name)
	}

	private fun createItem(material: Material, amount: Int, displayName: String, color: ChatColor?): ItemStack {
		val item = ItemStack(material, amount)
		val meta = item.itemMeta
		meta?.setDisplayName(color.toString() + displayName)
		item.itemMeta = meta
		return item
	}

	private fun createPlayerHead(name: String, value: String, color: ChatColor): ItemStack {
		val playerHead = ItemStack(Material.PLAYER_HEAD)
		val skullMeta = playerHead.itemMeta as SkullMeta

		val profile = Bukkit.createProfile(UUID.randomUUID(), null)
		profile.setProperty(ProfileProperty("textures", value))
		skullMeta.playerProfile = profile

		skullMeta.setDisplayName(color.toString() + name)
		playerHead.itemMeta = skullMeta

		return playerHead
	}
}